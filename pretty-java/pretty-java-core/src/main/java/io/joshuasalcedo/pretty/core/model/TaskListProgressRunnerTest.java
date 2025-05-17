package io.joshuasalcedo.pretty.core.model;

/**
 * A simple test program for the TaskListProgressRunner.
 */
public class TaskListProgressRunnerTest {

    public static void main(String[] args) {
        try {
            // Create a task list for a deployment process
            TaskListProgressRunner taskList = (TaskListProgressRunner) ProgressRunnerFactory.createTaskList(
                    "Deploying application to production:",
                    "Run unit tests",
                    "Build application",
                    "Deploy to staging environment",
                    "Run integration tests",
                    "Deploy to production",
                    "Verify production deployment",
                    "Update documentation"
            );

            // Start the task list
            taskList.start();

            // Step 1: Run unit tests
            taskList.markTaskInProgress(0);
            Thread.sleep(1000);
            taskList.markTaskComplete(0);

            // Step 2: Build application
            taskList.markTaskInProgress(1);
            Thread.sleep(1500);
            taskList.markTaskComplete(1);

            // Step 3: Deploy to staging
            taskList.markTaskInProgress(2);
            Thread.sleep(1200);
            taskList.markTaskComplete(2);

            // Step 4: Run integration tests
            taskList.markTaskInProgress(3);
            Thread.sleep(1800);
            taskList.markTaskComplete(3);

            // Step 5: Deploy to production
            taskList.markTaskInProgress(4);
            Thread.sleep(1500);
            taskList.markTaskComplete(4);

            // Step 6: Verify production
            taskList.markTaskInProgress(5);
            Thread.sleep(1000);
            taskList.markTaskComplete(5);

            // Step 7: Update documentation
            taskList.markTaskInProgress(6);
            Thread.sleep(1200);
            taskList.markTaskComplete(6);

            // Deployment complete
            Thread.sleep(500);
            taskList.stop();

            // Display summary
            System.out.println("✓ Application successfully deployed to production");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}