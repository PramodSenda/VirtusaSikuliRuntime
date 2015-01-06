package com.virtusa.isq.vtaf.sikuli.helper;



import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.virtusa.isq.vtaf.report.reporter.Reporter;
import com.virtusa.isq.vtaf.utils.PropertyHandler;

/**
 * The Class SikuliTestNgHelperVir.
 */
public class SikuliTestNgHelperVir  {
    
    /** The Mouse. */
    private static Mouse mouse;
    
    /** The KeyBoard. */
    private static Keyboard keyboard;
    
    /** The errorMessages. */
    private String errorMessages = "Verification failures : \n";
    
    /** The currentMethod. */
    private String currentMethod = "";
    
    /** The callingClassName. */
    private String callingClassName = "";
    
    /** The lineNumber. */
    private int lineNumber = 0;
    
    /** The RETRY. */
    private int retryCount;
    
    
    /** The minRecQuality. */
    private double minRecQuality;
    
    /** The maxRecQuality. */
    private double maxRecQuality;
    
    /** The testPackageName. */
    private String testPackageName = "";
    
    /** The reporter. */
    private static Reporter reporter;
    
    /** The identifire. */
    private static String identifire = "";
    
    /** The Properties. */
    private static Properties execProps;
    
    
    /**
     * @return the mouse
     */
    public static final Mouse getMouse() {
        return mouse;
    }

    /**
     * @param mouseObj
     *            the mouseObj to set
     */
    public static final void setMouse(final Mouse mouseObj) {
        SikuliTestNgHelperVir.mouse = mouseObj;
    }
    
    /**
     * @return the keyboard
     */
    public static final Keyboard getKeyboard() {
        return keyboard;
    }

    /**
     * @param keyboardObj
     *            the keyboardObj to set
     */
    public static final void setKeyboard(final Keyboard keyboardObj) {
        SikuliTestNgHelperVir.keyboard = keyboardObj;
    }
    
    /**
     * @return the errorMessage
     */
    public final String getErrorMessages() {
        return errorMessages;
    }

    /**
     * @param errorMessagesObj
     *            the errorMessagesObj to set
     */
    public final void setErrorMessages(final String errorMessagesObj) {
        this.errorMessages = errorMessagesObj;
    }
    
    /**
     * @return the currentMethod
     */
    public final String getCurrentMethod() {
        return currentMethod;
    }

    /**
     * @param currentMethodObj
     *            the currentMethodObj to set
     */
    public final void setCurrentMethod(final String currentMethodObj) {
        this.currentMethod = currentMethodObj;
    }

    /**
     * @return the callingClassName
     */
    public final String getCallingClassName() {
        return callingClassName;
    }

    /**
     * @param callingClassNameObj
     *            the callingClassNameObj to set
     */
    public final void setCallingClassName(final String callingClassNameObj) {
        this.callingClassName = callingClassNameObj;
    }

    /**
     * @return the lineNumber
     */
    public final int getLineNumber() {
        return lineNumber;
    }

    /**
     * @param lineNumberObj
     *            the lineNumberObj to set
     */
    public final void setLineNumber(final int lineNumberObj) {
        this.lineNumber = lineNumberObj;
    }

    /**
     * @return the RETRY
     */
    public final int getRetry() {
        return retryCount;
    }

    /**
     * @param retry
     *            the RetryCount to set
     */
    public final void setRetry(final int retry) {
        retryCount = retry;
    }

    /**
     * @return the minRecQuality
     */
    public final double getMinRecQuality() {
        return minRecQuality;
    }

    /**
     * @param minRecQualityObj
     *            the minRecQualityObj to set
     */
    public final void setMinRecQuality(final double minRecQualityObj) {
        this.minRecQuality = minRecQualityObj;
    }

    /**
     * @return the maxRecQuality
     */
    public final double getMaxRecQuality() {
        return maxRecQuality;
    }

    /**
     * @param maxRecQualityObj
     *            the maxRecQualityObj to set
     */
    public final void setMaxRecQuality(final double maxRecQualityObj) {
        this.maxRecQuality = maxRecQualityObj;
    }

    /**
     * @return the testPackageName
     */
    public final String getTestPackageName() {
        return testPackageName;
    }

    /**
     * @param testPackageNameObj
     *            the testPackageNameObj to set
     */
    public final void setTestPackageName(final String testPackageNameObj) {
        this.testPackageName = testPackageNameObj;
    }

    /**
     * @return the reporter
     */
    public static final Reporter getReporter() {
        return reporter;
    }

    /**
     * @param reporterObj
     *            the reporterObj to set
     */
    public static final void setReporter(final Reporter reporterObj) {
        SikuliTestNgHelperVir.reporter = reporterObj;
    }

    /**
     * @return the identifire
     */
    public static final String getIdentifire() {
        return identifire;
    }
    
    /**
     * @param identifireObj
     *              the identifireObj
     */
    public static final void setIdentifire(final String identifireObj) {
        SikuliTestNgHelperVir.identifire = identifireObj;
    }

    /**
     * @return the Properties
     */
    public static final Properties getExecProps() {
        return execProps;
    }
    
    /**
     * @param execPropsObj
     *            the execProps to set
     */
    public static final void setExecProps(final Properties execPropsObj) {
        SikuliTestNgHelperVir.execProps = execPropsObj;
    }
    
    /**
     * Sets the before test configuration for the test.
     * 
     * */
    @BeforeTest
    public final void setUp() {
        setReporter(new Reporter());
        getReporter().addNewTestExecution();
    }

    /**
     * beforesuite.
     * 
     * @throws IOException
     *             Signals that an I/O exception  occurred.
     */
    @BeforeSuite
    public final void beforesuite() throws IOException {
        this.readUserProp();
    }

    /**
     * before class.
     * 
     * @throws IOException
     *              throws I/O Exception.
     */
    @BeforeClass
    public final void beforeclass() throws IOException {

        getReporter().addNewTestSuite(this.getClass().getSimpleName());
    }

    /**
     * Sets the test context.
     * 
     * @param method
     *            the new test context
     */
    @BeforeMethod
    public final void setTestContext(final Method method) {
        getReporter().addNewTestCase(method.getName());
        setMouse(new DesktopMouse());
        setKeyboard(new DesktopKeyboard());
    }

    /**
     * Reportresult.
     * 
     * @param isAssert
     *            the is assert
     * @param step
     *            the step
     * @param result
     *            the result
     * @param messageString
     *            the message string
     */
    /**
     * Reportresult.
     * 
     * @param isAssert
     *            the is assert
     * @param step
     *            the step
     * @param result
     *            the result
     * @param messageString
     *            the message string
     */
    public final void reportresult(final boolean isAssert, final String step,
            final String result, final String messageString) {
        String message = messageString;

        StackTraceElement[] stackTraceElements =
                Thread.currentThread().getStackTrace();
        setCallingClassName(stackTraceElements[0].getClassName());
        
        for (int i = 0; i < stackTraceElements.length; i++) {
            setCallingClassName(stackTraceElements[i].getClassName());
            if (getCallingClassName().startsWith(getTestPackageName())) {
                setCallingClassName(stackTraceElements[i].getMethodName());
                setLineNumber(stackTraceElements[i].getLineNumber());
                break;
            }
        }
        
        // Adding data to the new reporter
        try {

            String testStep = step.substring(0, step.indexOf(':'));

            // replace xml special characters in the message.
            message = replaceXMLSpecialCharacters(message);
            if ("PASSED".equals(result)) {
                String testMessage = message;
                String stepDesc =
                        step.substring(step.indexOf(':') + 1, step.length());
                if ("".equals(message) || message == null) {
                    testMessage = stepDesc;
                }
                reporter.reportStepResults(true, testStep, testMessage,
                        "Success", "");
            } else {
                reporter.reportStepResults(false, testStep, message,
                        "Error",
                        getSourceLines(new Throwable(message).getStackTrace()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Replace xml special characters.
     * 
     * @param text
     *            the text
     * @return the string
     */
    public final String replaceXMLSpecialCharacters(final String text) {
        String replaced = text;

        return replaced.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;").replaceAll("'", "&apos;")
                .replaceAll("\"", "&quot;").replaceAll("»", "");
    }

    /**
     * afterTest.
     */
    @AfterTest
    public final void afterTest() {
        getReporter().endTestReporting();

    }

    /**
     * Gets the source lines.
     * 
     * @param stackTrace
     *            the stack trace
     * @return the source lines
     */
    public final String getSourceLines(final StackTraceElement[] stackTrace) {
        StringBuilder stacktraceLines = new StringBuilder();
        final int stackTraceIndex = 1;
        for (int elementid = 0; elementid < stackTrace.length; elementid++) {

            if (stackTrace[elementid].toString().indexOf("invoke0") != -1) {

                stacktraceLines.append(stackTrace[elementid - stackTraceIndex])
                        .append("|");
                stacktraceLines.append(
                        stackTrace[elementid - (stackTraceIndex + 1)]).append(
                        "|");
                stacktraceLines.append(stackTrace[elementid
                        - (stackTraceIndex + 2)]);
            }

        }
        return stacktraceLines.toString();
    }

    /**
     * Read user prop.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void readUserProp() throws IOException {

        PropertyHandler propHandler = new PropertyHandler("runtime.properties");

        
        String retry = propHandler.getRuntimeProperty("RETRY");
        String minrec = propHandler.getRuntimeProperty("MIN_REG_QUALITY");
        String maxrec = propHandler.getRuntimeProperty("MAX_REG_QUALITY");
        String rotDegree = propHandler.getRuntimeProperty("ROTATION_DEGREE");
        setExecProps(propHandler.getPropertyObject());
        try {
            
            if (!retry.isEmpty()) {
                setRetry(Integer.parseInt(retry));
            }
            if (!minrec.isEmpty()) {
                setMinRecQuality(Double.parseDouble(minrec));
            }
            if (!maxrec.isEmpty()) {
                setMinRecQuality(Double.parseDouble(maxrec));
            }
            if (!rotDegree.isEmpty()) {
                setMinRecQuality(Double.parseDouble(rotDegree));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        }

}
