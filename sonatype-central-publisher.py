#!/usr/bin/env python3
"""
Sonatype Central Publisher API Client

This script provides functionality to interact with the Sonatype Central Publisher API
for uploading, checking status, publishing, and dropping deployments to Maven Central.

Usage:
  python central_publisher.py upload <bundle_path> [--name=<name>] [--auto-publish]
  python central_publisher.py status <deployment_id>
  python central_publisher.py publish <deployment_id>
  python central_publisher.py drop <deployment_id>
  python central_publisher.py setup

Environment variables:
  SONATYPE_USERNAME - Your Sonatype username
  SONATYPE_PASSWORD - Your Sonatype password or token
"""

import argparse
import base64
import json
import os
import requests
import sys
import time
from getpass import getpass
from pathlib import Path

BASE_URL = "https://central.sonatype.com/api/v1/publisher"

def get_auth_header():
    """Generate the Authorization header using environment variables or prompt the user."""
    username = os.environ.get("SONATYPE_USERNAME")
    password = os.environ.get("SONATYPE_PASSWORD")
    
    if not username or not password:
        print("Credentials not found in environment variables.")
        username = input("Enter your Sonatype username: ")
        password = getpass("Enter your Sonatype password/token: ")
    
    auth_string = f"{username}:{password}"
    auth_base64 = base64.b64encode(auth_string.encode()).decode()
    return {"Authorization": f"Bearer {auth_base64}"}

def upload_bundle(bundle_path, name=None, auto_publish=False):
    """
    Upload a deployment bundle to Maven Central.
    
    Args:
        bundle_path: Path to the bundle zip file
        name: Optional human-readable name for the deployment
        auto_publish: Whether to automatically publish after validation
    
    Returns:
        The deployment ID if successful
    """
    print(f"Uploading bundle: {bundle_path}")
    
    # Prepare parameters
    url = f"{BASE_URL}/upload"
    params = {}
    if name:
        params["name"] = name
    if auto_publish:
        params["publishingType"] = "AUTOMATIC"
    
    # Prepare the file for upload
    files = {
        "bundle": (Path(bundle_path).name, open(bundle_path, "rb"), "application/octet-stream")
    }
    
    # Make the request
    try:
        response = requests.post(
            url,
            headers=get_auth_header(),
            params=params,
            files=files
        )
        
        if response.status_code == 201:
            deployment_id = response.text.strip()
            print(f"Upload successful! Deployment ID: {deployment_id}")
            return deployment_id
        else:
            print(f"Error uploading bundle: {response.status_code}")
            print(response.text)
            return None
    except Exception as e:
        print(f"Error uploading bundle: {str(e)}")
        return None
    finally:
        # Ensure file is closed
        files["bundle"][1].close()

def check_deployment_status(deployment_id):
    """
    Check the status of a deployment.
    
    Args:
        deployment_id: The ID of the deployment to check
    
    Returns:
        The deployment status information
    """
    print(f"Checking status for deployment: {deployment_id}")
    
    url = f"{BASE_URL}/status"
    params = {"id": deployment_id}
    
    try:
        response = requests.post(
            url,
            headers=get_auth_header(),
            params=params
        )
        
        if response.status_code == 200:
            status = response.json()
            print(f"Deployment state: {status.get('deploymentState', 'UNKNOWN')}")
            
            # Pretty print the status
            print("\nDeployment Details:")
            print(f"  ID: {status.get('deploymentId')}")
            print(f"  Name: {status.get('deploymentName')}")
            print(f"  State: {status.get('deploymentState')}")
            
            if 'purls' in status and status['purls']:
                print("\nPackage URLs:")
                for purl in status['purls']:
                    print(f"  - {purl}")
            
            if 'errors' in status and status['errors']:
                print("\nErrors:")
                for error in status['errors']:
                    print(f"  - {error}")
            
            return status
        else:
            print(f"Error checking status: {response.status_code}")
            print(response.text)
            return None
    except Exception as e:
        print(f"Error checking status: {str(e)}")
        return None

def publish_deployment(deployment_id):
    """
    Publish a validated deployment to Maven Central.
    
    Args:
        deployment_id: The ID of the deployment to publish
    
    Returns:
        True if successful, False otherwise
    """
    print(f"Publishing deployment: {deployment_id}")
    
    url = f"{BASE_URL}/deployment/{deployment_id}"
    
    try:
        response = requests.post(
            url,
            headers=get_auth_header()
        )
        
        if response.status_code == 204:
            print("Deployment published successfully!")
            return True
        else:
            print(f"Error publishing deployment: {response.status_code}")
            print(response.text)
            return False
    except Exception as e:
        print(f"Error publishing deployment: {str(e)}")
        return False

def drop_deployment(deployment_id):
    """
    Drop a deployment that is in VALIDATED or FAILED state.
    
    Args:
        deployment_id: The ID of the deployment to drop
    
    Returns:
        True if successful, False otherwise
    """
    print(f"Dropping deployment: {deployment_id}")
    
    url = f"{BASE_URL}/deployment/{deployment_id}"
    
    try:
        response = requests.delete(
            url,
            headers=get_auth_header()
        )
        
        if response.status_code == 204:
            print("Deployment dropped successfully!")
            return True
        else:
            print(f"Error dropping deployment: {response.status_code}")
            print(response.text)
            return False
    except Exception as e:
        print(f"Error dropping deployment: {str(e)}")
        return False

def wait_for_deployment(deployment_id, target_state="PUBLISHED", check_interval=10, timeout=1800):
    """
    Wait for a deployment to reach a specific state.
    
    Args:
        deployment_id: The ID of the deployment to check
        target_state: The state to wait for (default: "PUBLISHED")
        check_interval: Time in seconds between status checks
        timeout: Maximum time to wait in seconds
    
    Returns:
        The final deployment status, or None if timeout was reached
    """
    print(f"Waiting for deployment to reach {target_state} state...")
    
    start_time = time.time()
    while time.time() - start_time < timeout:
        status = check_deployment_status(deployment_id)
        
        if not status:
            print("Failed to check status, retrying...")
            time.sleep(check_interval)
            continue
        
        current_state = status.get('deploymentState')
        
        if current_state == target_state:
            print(f"Deployment reached {target_state} state!")
            return status
        
        if current_state == "FAILED":
            print("Deployment failed!")
            if 'errors' in status:
                print("\nErrors:")
                for error in status.get('errors', []):
                    print(f"  - {error}")
            return status
        
        print(f"Current state: {current_state}, waiting {check_interval} seconds...")
        time.sleep(check_interval)
    
    print(f"Timeout reached. Deployment did not reach {target_state} state within {timeout} seconds.")
    return None

def setup_credentials():
    """
    Set up Sonatype credentials and save them to .env file.
    """
    print("Setting up Sonatype credentials...")
    
    username = input("Enter your Sonatype username: ")
    password = getpass("Enter your Sonatype password/token: ")
    
    # Save to .env file
    with open(".env", "w") as f:
        f.write(f"SONATYPE_USERNAME={username}\n")
        f.write(f"SONATYPE_PASSWORD={password}\n")
    
    print("Credentials saved to .env file. You can now source this file or set the environment variables manually.")
    print("For bash: source .env")
    print("For PowerShell: Get-Content .env | ForEach-Object { $var = $_.Split('='); if($var[0] -and $var[1]) { [Environment]::SetEnvironmentVariable($var[0], $var[1], 'Process') } }")
    
    # Generate settings.xml snippet
    auth_string = f"{username}:{password}"
    auth_base64 = base64.b64encode(auth_string.encode()).decode()
    
    print("\nMaven settings.xml snippet for manual testing:")
    settings_xml = f'''
<settings>
  <servers>
    <server>
      <id>central.manual.testing</id>
      <configuration>
        <httpHeaders>
          <property>
            <name>Authorization</name>
            <value>Bearer {auth_base64}</value>
          </property>
        </httpHeaders>
      </configuration>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>central.manual.testing</id>
      <repositories>
        <repository>
          <id>central.manual.testing</id>
          <name>Central Testing repository</name>
          <url>https://central.sonatype.com/api/v1/publisher/deployments/download</url>
        </repository>
      </repositories>
    </profile>
  </profiles>
</settings>
'''
    print(settings_xml)
    
    # Save settings.xml snippet to file
    with open("central_testing_settings.xml", "w") as f:
        f.write(settings_xml)
    
    print("Settings saved to central_testing_settings.xml")
    
    return True

def main():
    parser = argparse.ArgumentParser(description="Sonatype Central Publisher API Client")
    subparsers = parser.add_subparsers(dest="command", help="Commands")
    
    # Upload command
    upload_parser = subparsers.add_parser("upload", help="Upload a deployment bundle")
    upload_parser.add_argument("bundle_path", help="Path to the bundle zip file")
    upload_parser.add_argument("--name", help="Human-readable name for the deployment")
    upload_parser.add_argument("--auto-publish", action="store_true", help="Automatically publish after validation")
    upload_parser.add_argument("--wait", action="store_true", help="Wait for deployment to complete")
    
    # Status command
    status_parser = subparsers.add_parser("status", help="Check deployment status")
    status_parser.add_argument("deployment_id", help="The deployment ID to check")
    
    # Publish command
    publish_parser = subparsers.add_parser("publish", help="Publish a validated deployment")
    publish_parser.add_argument("deployment_id", help="The deployment ID to publish")
    publish_parser.add_argument("--wait", action="store_true", help="Wait for deployment to complete")
    
    # Drop command
    drop_parser = subparsers.add_parser("drop", help="Drop a deployment")
    drop_parser.add_argument("deployment_id", help="The deployment ID to drop")
    
    # Setup command
    setup_parser = subparsers.add_parser("setup", help="Set up Sonatype credentials")
    
    args = parser.parse_args()
    
    if args.command == "upload":
        deployment_id = upload_bundle(args.bundle_path, args.name, args.auto_publish)
        if deployment_id and args.wait:
            wait_for_deployment(deployment_id)
    
    elif args.command == "status":
        check_deployment_status(args.deployment_id)
    
    elif args.command == "publish":
        success = publish_deployment(args.deployment_id)
        if success and args.wait:
            wait_for_deployment(args.deployment_id)
    
    elif args.command == "drop":
        drop_deployment(args.deployment_id)
    
    elif args.command == "setup":
        setup_credentials()
    
    else:
        parser.print_help()

if __name__ == "__main__":
    main()
