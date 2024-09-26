import java.io.FileInputStream;
    import java.io.IOException;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Properties;
    import java.util.logging.*;

    public class StackEmitter {
        private static final Logger logger = Logger.getLogger(StackEmitter.class.getName());

        public static void main(String[] args) {
            // Configure Logger
            System.setProperty("java.util.logging.SimpleFormatter.format","%1$tFT%1$tT%1$tz %4$s %5$s%6$s%n");
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            if (handlers[0] instanceof ConsoleHandler) {
                handlers[0].setLevel(Level.INFO);
            }
            logger.setLevel(Level.INFO);

            // Load Configuration
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream("/app/config.properties")) {
                properties.load(fis);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to load configuration file.", e);
                System.exit(1);
            }

            // Get Interval
            int intervalSeconds = 60; // Default interval
            String intervalStr = properties.getProperty("interval_seconds");
            if (intervalStr != null) {
                try {
                    intervalSeconds = Integer.parseInt(intervalStr);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid interval_seconds value. Using default 60 seconds.");
                }
            }

            // Get Exception Types
            String exceptionsStr = properties.getProperty("exception_types");
            List<String> exceptionTypes = Arrays.asList();
            if (exceptionsStr != null && !exceptionsStr.trim().isEmpty()) {
                exceptionTypes = Arrays.asList(exceptionsStr.split(","));
            }

            if (exceptionTypes.isEmpty()) {
                logger.warning("No exception types found in configuration.");
                System.exit(1);
            }

            logger.info("Starting stack emitter with interval " + intervalSeconds + " seconds.");

            while (true) {
                for (int i = 0; i < exceptionTypes.size(); i++) {
                    String exceptionType = exceptionTypes.get(i).trim();
                    logger.info("Emitting stack trace #" + (i + 1) + " (" + exceptionType + ")");
                    try {
                        throwException(exceptionType);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Emitted Stack Trace:", e);
                    }
                }
                try {
                    Thread.sleep(intervalSeconds * 1000L);
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Stack emitter interrupted.", e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private static void throwException(String exceptionType) throws Exception {
            switch (exceptionType) {
                case "NullPointerException":
                    simulateNullPointerException();
                    break;
                case "IllegalArgumentException":
                    simulateIllegalArgumentException();
                    break;
                case "IndexOutOfBoundsException":
                    simulateIndexOutOfBoundsException();
                    break;
                case "ArithmeticException":
                    simulateArithmeticException();
                    break;
                case "ClassNotFoundException":
                    simulateClassNotFoundException();
                    break;
                case "IOException":
                    simulateIOException();
                    break;
                case "NumberFormatException":
                    simulateNumberFormatException();
                    break;
                default:
                    logger.warning("Unsupported exception type: " + exceptionType);
            }
        }

        private static void simulateNullPointerException() {
            String str = null;
            // This will throw NullPointerException
            str.length();
        }

        private static void simulateIllegalArgumentException() {
            // This will throw IllegalArgumentException
            throw new IllegalArgumentException("Simulated IllegalArgumentException");
        }

        private static void simulateIndexOutOfBoundsException() {
            int[] arr = new int[3];
            // This will throw IndexOutOfBoundsException
            int num = arr[5];
        }

        private static void simulateArithmeticException() {
            // This will throw ArithmeticException
            int result = 10 / 0;
        }

        private static void simulateClassNotFoundException() throws ClassNotFoundException {
            // This will throw ClassNotFoundException
            Class.forName("com.nonexistent.ClassName");
        }

        private static void simulateIOException() throws IOException {
            // This will throw IOException
            throw new IOException("Simulated IOException");
        }

        private static void simulateNumberFormatException() {
            // This will throw NumberFormatException
            Integer.parseInt("not_a_number");
        }
    }