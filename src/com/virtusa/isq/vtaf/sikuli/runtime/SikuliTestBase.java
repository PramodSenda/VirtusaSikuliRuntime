package com.virtusa.isq.vtaf.sikuli.runtime;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.testng.ITestResult;
import org.testng.Reporter;






import com.virtusa.isq.vtaf.sikuli.helper.SikuliTestNgHelperVir;
import com.virtusa.isq.vtaf.utils.PropertyHandler;
import com.virtusa.isq.vtaf.objectmap.ObjectMap;


/**
 * The Class SikuliTestBase.
 */
public class SikuliTestBase extends SikuliTestNgHelperVir {

    
    /** The tables Hash Map. */
    private HashMap<String, DataTable> tables = null;
    
    /** The Retry Interval. */
    private static final int RETRY_INTERVAL = 1000;

    /**
     * Click.
     * Clicks on a given image.<br>
     *
     * @param objectName the objectName to set.
     */
    public final void click(final String objectName) {

        String path = "";
        int counter = getRetry();
        try {
            path = ObjectMap.getObjectSearchPath(objectName, "");
            ScreenRegion screen = isImagePresent(path, false);
            if (screen != null) {
                while (counter > 0) {
                    counter--;
                    try {
                        getMouse().click(screen.getCenter());
                        reportresult(true, "Click:", "PASSED", objectName + "");
                        break;
                    } catch (Exception e) {
                       
                        pause(RETRY_INTERVAL);
                        
                        if (!(counter > 0)) {
                            e.printStackTrace();
                            reportresult(true, "Click:", "FAILED",
                                    "Click command cannot access the Element :- "
                                            + objectName + " ");
                            checkTrue(false, true,
                                    "Click command cannot access the Element :-"
                                            + objectName + " ");
                            break;
                        }
                    }

                }
            } else {
                reportresult(true, "Click:", "FAILED",
                        "Click command cannot find the image :- " + path
                                + " in current screen.");
                checkTrue(false, true,
                        "Click command cannot find the image :- " + path
                                + " in current screen.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reportresult(true, "Click:", "FAILED",
                    "Click command trying to access Invalid Image :- " + path
                            + "");
            checkTrue(false, true,
                    "Click command trying to access Invalid Image :- " + path
                            + "");
        }

    }

    /**
     * Type.
     *
     * @param objectName the objectName to set.
     * @param text the objectName to text.
     * @throws InterruptedException throws InterruptedException.
     */
    public final void type(final String objectName, final String text)
            throws InterruptedException {

        String path = "";
        int counter = getRetry();
        try {
            path = ObjectMap.getObjectSearchPath(objectName, "");
            ScreenRegion screen = isImagePresent(path, false);
            if (screen != null) {
                while (counter > 0) {
                    counter--;
                    try {
                        getMouse().click(screen.getCenter());
                        getKeyboard().type(text);
                        reportresult(true, "Type:", "PASSED", objectName + "");
                        break;
                    } catch (Exception e) {
                        pause(RETRY_INTERVAL);
                        if (!(counter > 0)) {
                            e.printStackTrace();
                            reportresult(true, "Type:", "FAILED",
                                    "Type command cannot access the Element :- "
                                            + objectName + " ");
                            checkTrue(false, true,
                                    "Type command cannot access the Element :-"
                                            + objectName + " ");
                            break;
                        }
                    }

                }
            } else {
                reportresult(true, "Type:", "FAILED",
                        "Type command cannot find the image :- " + path
                                + " in current screen.");
                checkTrue(false, true, "Type command cannot find the image :- "
                        + path + " in current screen.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reportresult(true, "Type:", "FAILED",
                    "Type command trying to access Invalid Image :- " + path
                            + "");
            checkTrue(false, true,
                    "Type command trying to access Invalid Image :- " + path
                            + "");
        }

    }
    
    /**
     * evaluate the given logical condition and report the result Further
     * execution or termination of test will be decided by the value of isAssert
     * variable.
     * 
     * @param checkingCondition
     *            the checking condition
     * @param isAssert
     *            the is assert
     * @param failedMessage
     *            the failed message
     */
    private void checkTrue(final boolean checkingCondition,
            final boolean isAssert, final String failedMessage) {
        String errorMessage = getErrorMessages();
        String callingClassName = getCallingClassName();
        String currentMethod = getCurrentMethod();
        int lineNumber = getLineNumber();
        if (isAssert) {

            
            assertTrue("Failed " + failedMessage + "\n" + errorMessage
                    + " [At : " + callingClassName + "." + currentMethod
                    + "(Line:" + lineNumber + ")]" + "\n", checkingCondition);
        } else {
            try {
                ITestResult reult;
                setErrorMessages(errorMessage + "\n" + failedMessage
                        + " [At : " + callingClassName + "." + currentMethod
                        + "(Line:" + lineNumber + ")]" + "\n");
                reult = Reporter.getCurrentTestResult();
                reult.setStatus(ITestResult.SUCCESS_PERCENTAGE_FAILURE);
                reult.setThrowable(new Exception(getErrorMessages()));
                Reporter.setCurrentTestResult(reult);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Checks if is image present.
     * 
     * @param path
     *            the path
     * @param isRotatable
     *            the is rotatable
     * @return the screen region
     * @throws FileNotFoundException
     *              throws if file is not avaliable
     */
    private ScreenRegion isImagePresent(final String path,
            final boolean isRotatable) throws FileNotFoundException {

        PropertyHandler propHandler = new PropertyHandler("runtime.properties");
        double maxRecQuality =
                Double.parseDouble(propHandler
                        .getRuntimeProperty("MAX_REG_QUALITY"));

        int rotationDegree =
                Integer.parseInt(propHandler
                        .getRuntimeProperty("ROTATION_DEGREE"));

        int retry = getRetry();
        double regQuality = maxRecQuality;
        Target target;
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        javaxt.io.Image img = new javaxt.io.Image(path);
        ScreenRegion targetRegion = null;
        ScreenRegion screen = new DesktopScreenRegion();
        target = new ImageTarget(img.getBufferedImage());
        target.setMinScore(regQuality);
        while (retry > 0) {
            retry--;
            targetRegion = screen.find(target);
            if (targetRegion == null) {
                pause(RETRY_INTERVAL);
                if (isRotatable) {
                    img.rotate(rotationDegree);
                }
            } else {
                break;
            }
        }

        return targetRegion;
    }

    /**
     * Check Element present.
     *
     * @param objectName the objectName
     * @param stopOnFailure the stop on failure
     * @param customError the custom error
     */
    public final void checkElementPresent(final String objectName,
             final boolean stopOnFailure, final Object... customError) {
        String path = "";
        boolean isElementPresent = false;
        try {
            path = ObjectMap.getObjectSearchPath(objectName, "");
            isElementPresent = isImagePresent(path, false) != null;
            if (isElementPresent) {
                reportresult(true, "Check Element Present:", "PASSED",
                        objectName);
                /*
                 * checkTrue(false, stopOnFailure,
                 * "Check Element Present command cannot find the image " +
                 * objectName + " in current screen.");
                 */
            } else {
                reportresult(true, generateCustomError(customError) + "Check Element Present:", "FAILED",
                        "CheckelementPresent command cannot find the image :- "
                                + objectName + " in current screen.");
                checkTrue(false, stopOnFailure,
                        generateCustomError(customError) + "CheckelementPresent command cannot find the image :- "
                                + objectName + " in current screen.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            reportresult(true, generateCustomError(customError) + "Check Element Present:", "FAILED",
                    "Trying to access Invalid Image :- " + objectName + "");
            checkTrue(false, stopOnFailure,
                    generateCustomError(customError) 
                    + "Check Element Present command:Trying to access Invalid Image :- "
                            + objectName + "");

        }

    }
    
    /**
     * Check Element present.
     * 
     * @param objectName
     *            the objectName
     * @return false
     *             if image is not present.
     */
    public final boolean checkElementPresent(final String objectName) {
        
        try {
            String path = ObjectMap.getObjectSearchPath(objectName, "");
            return isImagePresent(path, false) != null;
            
            } catch (FileNotFoundException e) {
        e.printStackTrace();
        return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;   
        }
        
    }
    
    /**
     * Check Image present.
     *
     * @param objectName the objectName
     * @param isRotatable the is rotatable
     * @param stopOnFailure the stop on failure
     * @param customError the custom error
     */
    public final void checkImagePresent(final String objectName, final boolean isRotatable,
            final boolean stopOnFailure, final Object... customError) {
        String path = "";
        boolean isElementPresent = false;
        try {
            path = ObjectMap.getObjectSearchPath(objectName, "");
            isElementPresent = isImagePresent(path, false) != null;
            if (isElementPresent) {
                reportresult(true, "Check Image Present:", "PASSED",
                        objectName);

            } else {
                reportresult(true, generateCustomError(customError) + "Check Image Present:", "FAILED",
                        "CheckImagePresent command cannot find the image :- "
                                + objectName + " in current screen.");
                checkTrue(false, stopOnFailure,
                        generateCustomError(customError) 
                        + "CheckImagePresent command cannot find the image :- "
                                + objectName + " in current screen.");

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            reportresult(true, "CHECK IMAGE PRESENT :", "FAILED",
                    "Given location is invalid :- " + path + "");
            checkTrue(false, stopOnFailure,
                    "CHECK IMAGE PRESENT command:Given location is invalid :- "
                            + path + "");
        } catch (Exception e) {
            e.printStackTrace();
            reportresult(true, generateCustomError(customError) + "Check Image Present:", "FAILED",
                    "Trying to access Invalid Image :- " + objectName + "");
            checkTrue(false, stopOnFailure,
                    generateCustomError(customError) + "Check Image Present command:Trying to access Invalid Image :- "
                            + objectName + "");

        }
    }
    
    /**
     * Generate custom error.
     *
     * @param customError the custom error
     * @return the string
     */
    private String generateCustomError(final Object[] customError) {
        String customErrorMessage = "";

        if (customError != null
                && !(customError[0].equals("null") || customError[0].equals(""))) {

            
            customErrorMessage =
                    " Custom Error : " + customError[0].toString() + ". ";
        }
        return customErrorMessage;
    }
    /**
     * Check Element present.
     * 
     * @param objectName
     *            the objectName
     * @param isRotatable
     *            the is rotatable
     * @return false
     *             if image is not present.
     * 
     */
    public final boolean checkImagePresent(final String objectName, final boolean isRotatable) {
         
        try {
            String path = ObjectMap.getObjectSearchPath(objectName, "");
            return isImagePresent(path, isRotatable) != null;
            
            } catch (FileNotFoundException e) {
        e.printStackTrace();
        return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }

    /**
     * Retrieve the data table for the parameterized executing.
     *
     * @param name the name
     * @return  the tables name.
     */
    public final DataTable getTable(final String name) {
        File file;
        if (tables == null) {
            File tempFile = new File("tempFile");
            System.out.println("running location :"
                    + tempFile.getAbsolutePath());
            
                file = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "data" + File.separator + "DataTables.xml");
                System.out.println("Location of data file is :"
                        + file.getAbsolutePath());
            
            tables = DataTablesParser.parseTables(file);
        }
        return tables.get(name);
    }

    /**
     * read the DataTable and convert it to a two dimentional array.
     * 
     * @param table
     *              the table.
     *              
     * @return Object.
     * 
     * */
    public final Object[][] getTableArray(final DataTable table) {
        Object[][] tabArray;
        Integer rowcount = table.getRowCount();
        Integer colcount = table.getcolCount();
        tabArray = new Object[rowcount][colcount];
        for (int row = 0; row < rowcount; row++) {
            for (int col = 0; col < colcount; col++) {
                tabArray[row][col] = table.get(row, col);
            }
        }
        return tabArray;
    }
    /**
     * Sleeps for the specified number of milliseconds.
     * 
     * @param millisecs
     *            the millisecs
     */
    public final void pause(final int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Failure.
     * 
     * @param message
     *            the message
     */
    public static void failure(final Object message) {
        throw new AssertionError(message.toString());

    }

    /**
     * Assert true.
     * 
     * @param message
     *            the message
     * @param condition
     *            the condition
     */
     public static void assertTrue(final String message, final boolean condition) {
        if (!condition) {
            failure(message);

        }

    }
     /**
      * clickAt.
      * 
      * @param objectName
      *            the objectName
      * @param identifire
      *            the is identifire
      * @param coordinateString
      *            the coordinateString
      */
     public final void clickAt(final String objectName, final String identifire,
             final String coordinateString) {

         setIdentifire(identifire);
         clickAt(objectName, coordinateString);
         setIdentifire("");
     }

     /**
      * clickAt.
      * 
      * @param objectName
      *            the objectName
      * @param coordinateString
      *            the coordinateString
      */
     public final void clickAt(final String objectName, final String coordinateString) {
         String path = "";
         int counter = getRetry();
         String xOffset = "";
         int yOffset = 0;
         Target target;
         ScreenRegion targetRegion = null;
         try {
             // Retrieve the correct object locator from the object map
             path = ObjectMap.getObjectSearchPath(objectName, "");
             targetRegion = isImagePresent(path, false);
             try {
                 xOffset = coordinateString.split(",")[0].trim();
                 yOffset = Integer.parseInt((coordinateString.split(",")[1])
                         .trim());
             } catch (NumberFormatException e) {

                 e.printStackTrace();
                 reportresult(true, "CLICKAT :" + objectName + "", "FAILED",
                         "CLICKAT coordinate string (" + coordinateString
                                 + ") for :Image (" + objectName + ") [" + path
                                 + "] is invalid");
                 checkTrue(false, true, "CLICKAT coordinate string ("
                         + coordinateString + ") " + "for :Image (" + objectName
                         + ") [" + path + "] is invalid");
             }
             /*
              * START DESCRIPTION following while loop was added to make the
              * command more consistent try the command for give amount of time
              * (can be configured through class variable RETRY) command will be
              * tried for "RETRY" amount of times until command works. any
              * exception thrown within the tries will be handled internally.
              * 
              * can be exited from the loop under 2 conditions 1. if the command
              * succeeded 2. if the RETRY count is exceeded
              */
             while (counter > 0) {
                 try {
                     counter--;
                     javaxt.io.Image img = new javaxt.io.Image(path);
                     target = new ImageTarget(img.getBufferedImage());
                     target.setMinScore(getMinRecQuality());
                     Mouse mouse = getMouse();
                     if ("right".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion).right(yOffset)
                                 .getScreenRegion();
                         mouse.click(targetRegion.getCenter());
                     } else if ("left".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .left(yOffset).getScreenRegion();
                         mouse.click(targetRegion.getCenter());
                     } else if ("above".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .above(yOffset).getScreenRegion();
                         mouse.click(targetRegion.getCenter());
                     } else if ("below".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .below(yOffset).getScreenRegion();
                         mouse.click(targetRegion.getCenter());
                     } else if ("wider".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .wider(yOffset).getScreenRegion();
                         mouse.click(targetRegion.getCenter());

                     } else if ("taller".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .taller(yOffset).getScreenRegion();
                         mouse.click(targetRegion.getCenter());
                     }

                     reportresult(true, "CLICKAT :" + objectName + "", "PASSED",
                             "");

                     break;
                 } catch (NullPointerException e) {
                     
                     pause(RETRY_INTERVAL);
                     if (!(counter > 0)) {

                         e.printStackTrace();
                         reportresult(true, "CLICKAT :" + objectName + "",
                                 "FAILED",
                                 "CLICKAT command cannot access Image ("
                                         + objectName + ") [" + path + "] ");
                         checkTrue(false, true,
                                 "CLICKAT command cannot access Image ("
                                         + objectName + ") [" + path + "] ");
                     }
                 }
             }
             /*
              * END DESCRIPTION
              */

         } catch (FileNotFoundException e) {

             e.printStackTrace();
             
             reportresult(true, "CLICKAT :" + objectName + "", "FAILED",
                     "CLICKAT command  : Trying to access Invalid image"
                             + objectName);
             checkTrue(false, true,
                     "CLICKAT command  : Trying to access Invalid image "
                             + objectName + "");
         }

     }

     /**
      * doubleClick.
      * 
      * @param objectName
      *            the objectName
      * 
      */
     public final void doubleClick(final String objectName) {

         String path = "";
         int counter = getRetry();
         try {
             path = ObjectMap.getObjectSearchPath(objectName, "");
             ScreenRegion targetRegion = isImagePresent(path, false);
             if (targetRegion != null) {
                 while (counter > 0) {
                     counter--;
                     try {
                         getMouse().doubleClick(targetRegion.getCenter());
                         reportresult(true, "Doubleclick:", "PASSED", objectName
                                 + "");
                         break;
                     } catch (Exception e) {
                         pause(RETRY_INTERVAL);
                         if (!(counter > 0)) {
                             e.printStackTrace();
                             reportresult(true, "Doubleclick:", "FAILED",
                                     "Doubleclick command cannot access the Element :- "
                                             + objectName + " ");
                             checkTrue(false, true,
                                     "Doubleclick command cannot access the Element :-"
                                             + objectName + " ");
                             break;
                         }
                     }

                 }
             } else {
                 reportresult(true, "Doubleclick:", "FAILED",
                         "Doubleclick command cannot find the image :- "
                                 + objectName + " in current screen.");
                 checkTrue(false, true,
                         "Doubleclick command cannot find the image :- "
                                 + objectName + " in current screen.");
             }
         } catch (FileNotFoundException e) {
             e.printStackTrace();
             reportresult(true, "Doubleclick:", "FAILED",
                     "Doubleclick command trying to access Invalid Image :- "
                             + objectName + "");
             checkTrue(false, true,
                     "Doubleclick command trying to access Invalid Image :- "
                             + objectName + "");
         }

     }

     /**
      * doubleClickAt.
      * 
      * @param objectName
      *            the objectName
      * @param coordinateString
      *            the coordinateString
      */
     public final void doubleClickAt(final String objectName,
             final String coordinateString) {
         String path = "";
         int counter = getRetry();
         String xOffset = "";
         int yOffset = 0;
         Target target;
         ScreenRegion targetRegion = null;
         try {
             // Retrieve the correct object locator from the object map
             path = ObjectMap.getObjectSearchPath(objectName, "");
             targetRegion = isImagePresent(path, false);
             try {
                 xOffset = coordinateString.split(",")[0].trim();
                 yOffset = Integer.parseInt((coordinateString.split(",")[1])
                         .trim());
             } catch (Exception e) {

                 e.printStackTrace();
                 reportresult(true, "DOUBLECLICKAT :" + objectName + "",
                         "FAILED", "DoubleclickAt coordinate string ("
                                 + coordinateString + ") for :Image ("
                                 + objectName + ") [" + path + "] is invalid");
                 checkTrue(false, true, "DoubleclickAt coordinate string ("
                         + coordinateString + ") " + "for :Image (" + objectName
                         + ") [" + path + "] is invalid");
             }
             /*
              * START DESCRIPTION following while loop was added to make the
              * command more consistent try the command for give amount of time
              * (can be configured through class variable RETRY) command will be
              * tried for "RETRY" amount of times until command works. any
              * exception thrown within the tries will be handled internally.
              * 
              * can be exited from the loop under 2 conditions 1. if the command
              * succeeded 2. if the RETRY count is exceeded
              */
             while (counter > 0) {
                 try {
                     counter--;
                     javaxt.io.Image img = new javaxt.io.Image(path);
                     target = new ImageTarget(img.getBufferedImage());
                     target.setMinScore(getMinRecQuality());
                     Mouse mouse = getMouse();
                     if ("right".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target)).right(yOffset)
                                 .getScreenRegion();
                         mouse.doubleClick(targetRegion.getCenter());
                     } else if ("left".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .left(yOffset).getScreenRegion();
                         mouse.doubleClick(targetRegion.getCenter());
                     } else if ("above".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .above(yOffset).getScreenRegion();
                         mouse.doubleClick(targetRegion.getCenter());
                     } else if ("below".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .below(yOffset).getScreenRegion();
                         mouse.doubleClick(targetRegion.getCenter());
                     } else if ("wider".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .wider(yOffset).getScreenRegion();
                         mouse.doubleClick(targetRegion.getCenter());

                     } else if ("taller".equals(xOffset)) {
                         targetRegion = Relative.to(targetRegion.find(target))
                                 .taller(yOffset).getScreenRegion();
                         mouse.doubleClick(targetRegion.getCenter());
                     }

                     reportresult(true, "DOUBLECLICKAT :" + objectName + "",
                             "PASSED", "");

                     break;
                 } catch (NullPointerException e) {
                     // TODO Auto-generated catch block
                     pause(RETRY_INTERVAL);
                     if (!(counter > 0)) {

                         e.printStackTrace();
                         reportresult(true, "DOUBLECLICKAT :" + objectName + "",
                                 "FAILED",
                                 "DOUBLECLICKAT command cannot access Image ("
                                         + objectName + ") [" + path + "] ");
                         checkTrue(false, true,
                                 "DOUBLECLICKAT command cannot access Image ("
                                         + objectName + ") [" + path + "] ");
                     }
                 }
             }
             /*
              * END DESCRIPTION
              */

         } catch (FileNotFoundException e) {
             // TODO Auto-generated catch block

             e.printStackTrace();
             /*
              * VTAF result reporter call
              */
             reportresult(true, "DOUBLECLICKAT :" + objectName + "", "FAILED",
                     "DOUBLECLICKAT command  : Trying to access Invalid image"
                             + objectName);

             /*
              * VTAF specific validation framework reporting
              */
             checkTrue(false, true,
                     "DOUBLECLICKAT command  : Trying to access Invalid image "
                             + objectName + "");
         }

     }

     /**
      * Drag and drop an image. Parameters - Pass two Objects
      * 
      * @param objectName
      *                 the objectName
      * @param objectName2
      *                 the second Object
      * */

     public final void dragAndDrop(final String objectName, final String objectName2) {

         String dragPath = "";
         String dropPath = "";
         int counter = getRetry();
         try {
             dragPath = ObjectMap.getObjectSearchPath(objectName, "");
             dropPath = ObjectMap.getObjectSearchPath(objectName2, "");

             ScreenRegion dragImage = isImagePresent(dragPath, false);
             if (dragImage == null) {
                 reportresult(true, "DragandDrop:", "FAILED",
                         "DragandDrop command can't find the Image :- "
                                 + objectName + " to Drag.");
                 checkTrue(false, true,
                         "DragandDrop command can't find the Image :- "
                                 + objectName + " to Drag.");
             }
             ScreenRegion dropImage = isImagePresent(dropPath, false);
             if (dropImage == null) {
                 reportresult(true, "DragandDrop:", "FAILED",
                         "DragandDrop command can't find the Image :- "
                                 + objectName2 + " to Drop.");
                 checkTrue(false, true,
                         "DragandDrop command can't find the Image :- "
                                 + objectName2 + " to Drop.");
             }
             Mouse mouse = getMouse();
             if (dragImage != null && dropImage != null) {
                 while (counter > 0) {
                     counter--;
                     try {

                         mouse.drag(dragImage.getCenter());
                         mouse.drop(dropImage.getCenter());
                         reportresult(true, "DragandDrop:", "PASSED", dragPath
                                 + "(" + objectName + " )dragged to " + dropPath
                                 + "(" + objectName2 + ")");
                         break;
                     } catch (Exception e) {
                         pause(RETRY_INTERVAL);
                         if (!(counter > 0)) {
                             e.printStackTrace();
                             reportresult(true, "DragandDrop:", "FAILED",
                                     "DragandDrop command cannot access the Image :- "
                                             + objectName + " ");
                             checkTrue(false, true,
                                     "DragandDrop command cannot access the Image :-"
                                             + objectName + " ");
                             break;
                         }
                     }
                 }
             }
         } catch (FileNotFoundException e) {
             e.printStackTrace();
             reportresult(true, "DragandDrop:", "FAILED",
                     "DragandDrop command trying to access Invalid Image :- "
                             + objectName + "");
             checkTrue(false, true,
                     "DragandDrop command trying to access Invalid Image :- "
                             + objectName + "");
         }
     }

     /**
      * Sleeps for the specified number of milliseconds.
      * @param waitingTime
      *                 the wait Time.
      * */
     public final void pause(final String waitingTime) {
         int waitingMilliSeconds = Integer.parseInt(waitingTime);
         try {
             pause(waitingMilliSeconds);
             reportresult(true, "PAUSE Command: (" + waitingMilliSeconds
                     + " ms)", "PASSED", "Pausing for " + waitingTime
                     + " Milliseconds.");
         } catch (Exception e) {
             reportresult(true, "PAUSE Command: ", "FAILED",
                     "Pause commad interrupted error : " + e.getMessage());
             e.printStackTrace();
         }
     }

}
