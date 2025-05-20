package io.joshuasalcedo.pretty.core.api;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;

public class PrettyPrintStream extends PrintStream {
    public PrettyPrintStream(OutputStream out) {
        super(out);
    }

    public PrettyPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrettyPrintStream(OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
    }

    public PrettyPrintStream(OutputStream out, boolean autoFlush, Charset charset) {
        super(out, autoFlush, charset);
    }

    public PrettyPrintStream(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public PrettyPrintStream(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public PrettyPrintStream(String fileName, Charset charset) throws IOException {
        super(fileName, charset);
    }

    public PrettyPrintStream(File file) throws FileNotFoundException {
        super(file);
    }

    public PrettyPrintStream(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    public PrettyPrintStream(File file, Charset charset) throws IOException {
        super(file, charset);
    }

    @Override
    public void flush() {
        super.flush();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean checkError() {
        return super.checkError();
    }

    @Override
    protected void setError() {
        super.setError();
    }

    @Override
    protected void clearError() {
        super.clearError();
    }

    @Override
    public void write(int b) {
        super.write(b);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
    }

    @Override
    public void write(byte[] buf) throws IOException {
        super.write(buf);
    }

    @Override
    public void writeBytes(byte[] buf) {
        super.writeBytes(buf);
    }

    @Override
    public void print(boolean b) {
        super.print(b);
    }

    @Override
    public void print(char c) {
        super.print(c);
    }

    @Override
    public void print(int i) {
        super.print(i);
    }

    @Override
    public void print(long l) {
        super.print(l);
    }

    @Override
    public void print(float f) {
        super.print(f);
    }

    @Override
    public void print(double d) {
        super.print(d);
    }

    @Override
    public void print(char[] s) {
        super.print(s);
    }

    @Override
    public void print(String s) {
        super.print(s);
    }

    @Override
    public void print(Object obj) {
        super.print(obj);
    }

    @Override
    public void println() {
        super.println();
    }

    @Override
    public void println(boolean x) {
        super.println(x);
    }

    @Override
    public void println(char x) {
        super.println(x);
    }

    @Override
    public void println(int x) {
        super.println(x);
    }

    @Override
    public void println(long x) {
        super.println(x);
    }

    @Override
    public void println(float x) {
        super.println(x);
    }

    @Override
    public void println(double x) {
        super.println(x);
    }

    @Override
    public void println(char[] x) {
        super.println(x);
    }

    @Override
    public void println(String x) {
        super.println(x);
    }

    @Override
    public void println(Object x) {
        super.println(x);
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        return super.printf(format, args);
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        return super.printf(l, format, args);
    }

    @Override
    public PrintStream format(String format, Object... args) {
        return super.format(format, args);
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
        return super.format(l, format, args);
    }

    @Override
    public PrintStream append(CharSequence csq) {
        return super.append(csq);
    }

    @Override
    public PrintStream append(CharSequence csq, int start, int end) {
        return super.append(csq, start, end);
    }

    @Override
    public PrintStream append(char c) {
        return super.append(c);
    }

    @Override
    public Charset charset() {
        return super.charset();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
