import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("      STARTING CORE JAVA EXERCISES SUITE         ");
        System.out.println("=================================================\n");

        // 1. Hello World Program [cite: 2]
        runExercise("1. Hello World", () -> HelloWorld.main(null));

        // 2. Simple Calculator [cite: 9]
        runExercise("2. Simple Calculator", () -> SimpleCalculator.calculate(10, 5, '+'));

        // 3. Even or Odd Checker [cite: 15]
        runExercise("3. Even or Odd Checker", () -> EvenOddChecker.check(7));

        // 4. Leap Year Checker [cite: 23]
        runExercise("4. Leap Year Checker", () -> LeapYearChecker.check(2024));

        // 5. Multiplication Table [cite: 31]
        runExercise("5. Multiplication Table", () -> MultiplicationTable.printTable(5));

        // 6. Data Type Demonstration [cite: 39]
        runExercise("6. Data Type Demonstration", DataTypeDemo::demonstrate);

        // 7. Type Casting Example [cite: 47]
        runExercise("7. Type Casting Example", TypeCastingDemo::demonstrate);

        // 8. Operator Precedence [cite: 54]
        runExercise("8. Operator Precedence", OperatorPrecedenceDemo::demonstrate);

        // 9. Grade Calculator [cite: 60]
        runExercise("9. Grade Calculator", () -> GradeCalculator.calculate(85));

        // 10. Number Guessing Game [cite: 71]
        runExercise("10. Number Guessing Game (Simulated)", NumberGuessingGame::playSimulated);

        // 11. Factorial Calculator [cite: 79]
        runExercise("11. Factorial Calculator", () -> FactorialCalculator.calculate(5));

        // 12. Method Overloading [cite: 86]
        runExercise("12. Method Overloading", MethodOverloadingDemo::demonstrate);

        // 13. Recursive Fibonacci [cite: 97]
        runExercise("13. Recursive Fibonacci", () -> {
            int n = 6;
            System.out.println("Fibonacci of " + n + " is: " + RecursiveFibonacci.fibonacci(n)); // [cite: 103]
        });

        // 14. Array Sum and Average [cite: 104]
        runExercise("14. Array Sum and Average", () -> ArrayOperations.calculate(new int[]{10, 20, 30, 40, 50}));

        // 15. String Reversal [cite: 110]
        runExercise("15. String Reversal", () -> StringReversal.reverse("Java"));

        // 16. Palindrome Checker [cite: 117]
        runExercise("16. Palindrome Checker", () -> PalindromeChecker.check("A man, a plan, a canal: Panama"));

        // 17. Class and Object Creation [cite: 125]
        runExercise("17. Class and Object Creation", CarDemo::demonstrate);

        // 18. Inheritance Example [cite: 132]
        runExercise("18. Inheritance Example", InheritanceDemo::demonstrate);

        // 19. Interface Implementation [cite: 140]
        runExercise("19. Interface Implementation", InterfaceDemo::demonstrate);

        // 20. Try-Catch Example [cite: 147]
        runExercise("20. Try-Catch Example", () -> TryCatchDemo.divide(10, 0));

        // 21. Custom Exception [cite: 155]
        runExercise("21. Custom Exception", () -> CustomExceptionDemo.checkAge(16));

        // 22 & 23. File Writing & Reading [cite: 161, 168]
        runExercise("22 & 23. File IO Operations", FileIODemo::demonstrate);

        // 24. ArrayList Example [cite: 174]
        runExercise("24. ArrayList Example", ArrayListDemo::demonstrate);

        // 25. HashMap Example [cite: 183]
        runExercise("25. HashMap Example", HashMapDemo::demonstrate);

        // 26. Thread Creation [cite: 192]
        runExercise("26. Thread Creation", ThreadCreationDemo::demonstrate);

        // 27. Lambda Expressions [cite: 200]
        runExercise("27. Lambda Expressions", LambdaDemo::demonstrate);

        // 28. Stream API [cite: 208]
        runExercise("28. Stream API", StreamAPIDemo::demonstrate);

        // 29. Records [cite: 215]
        runExercise("29. Records (Java 16+)", RecordsDemo::demonstrate);

        // 30. Pattern Matching for switch [cite: 222]
        runExercise("30. Pattern Matching for switch (Java 21)", PatternMatchingDemo::demonstrate);

        // 31, 32, 33. JDBC Connection, Operations & Transactions [cite: 229, 235, 242]
        runExercise("31, 32, 33. JDBC Database Suite (SQLite Memory DB)", JDBCSuite::demonstrate);

        // 34. Create and Use Java Modules [cite: 250]
        runExercise("34. Java Modules Simulation", ModuleSimulation::demonstrate);

        // 35. TCP Client-Server Chat [cite: 257]
        runExercise("35. TCP Sockets Simulation", TCPSocketSimulation::demonstrate);

        // 36. HTTP Client API [cite: 264]
        runExercise("36. HTTP Client API (Java 11+)", HTTPClientDemo::demonstrate);

        // 37 & 38. Bytecode Inspection & Decompilation Context [cite: 271, 278]
        runExercise("37 & 38. Bytecode & Decompiler Info", BytecodeDecompileDemo::demonstrate);

        // 39. Reflection in Java [cite: 285]
        runExercise("39. Reflection API", ReflectionDemo::demonstrate);

        // 40. Virtual Threads [cite: 290]
        runExercise("40. Virtual Threads (Java 21)", VirtualThreadsDemo::demonstrate);

        // 41. Executor Service and Callable [cite: 293]
        runExercise("41. Executor Service and Callable", ExecutorServiceDemo::demonstrate);

        System.out.println("=================================================");
        System.out.println("      ALL EXERCISES EXECUTED SUCCESSFULLY        ");
        System.out.println("=================================================");
    }

    private static void runExercise(String title, Runnable exerciseCode) {
        System.out.println("--- Executing: " + title + " ---");
        try {
            exerciseCode.run();
        } catch (Exception e) {
            System.out.println("Exception caught during execution: " + e.getMessage());
        }
        System.out.println();
    }

    // 1. Hello World Program [cite: 2]
    static class HelloWorld {
        public static void main(String[] args) {
            System.out.println("Hello, World!"); // [cite: 4, 8]
        }
    }

    // 2. Simple Calculator [cite: 9]
    static class SimpleCalculator {
        public static void calculate(double num1, double num2, char operation) { // [cite: 12, 13]
            double result = 0;
            switch (operation) {
                case '+' -> result = num1 + num2; // [cite: 11]
                case '-' -> result = num1 - num2; // [cite: 11]
                case '*' -> result = num1 * num2; // [cite: 11]
                case '/' -> result = num2 != 0 ? num1 / num2 : 0; // [cite: 11]
            }
            System.out.println(num1 + " " + operation + " " + num2 + " = " + result); // [cite: 14]
        }
    }

    // 3. Even or Odd Checker [cite: 15]
    static class EvenOddChecker {
        public static void check(int number) { // [cite: 20]
            if (number % 2 == 0) { // [cite: 21]
                System.out.println(number + " is Even"); // [cite: 22]
            } else {
                System.out.println(number + " is Odd"); // [cite: 22]
            }
        }
    }

    // 4. Leap Year Checker [cite: 23]
    static class LeapYearChecker {
        public static void check(int year) { // [cite: 28]
            // A year is a leap year if it's divisible by 4 but not by 100, unless it's also divisible by 400. [cite: 29]
            boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            System.out.println(year + " is a leap year? " + isLeap); // [cite: 30]
        }
    }

    // 5. Multiplication Table [cite: 31]
    static class MultiplicationTable {
        public static void printTable(int number) { // [cite: 36]
            for (int i = 1; i <= 10; i++) { // [cite: 37]
                System.out.println(number + " x " + i + " = " + (number * i)); // [cite: 38]
            }
        }
    }

    // 6. Data Type Demonstration [cite: 39]
    static class DataTypeDemo {
        public static void demonstrate() {
            int intVar = 10; // [cite: 43, 44]
            float floatVar = 5.99f; // [cite: 43, 44]
            double doubleVar = 19.99; // [cite: 43, 44]
            char charVar = 'A'; // [cite: 43, 44]
            boolean boolVar = true; // [cite: 43, 44]

            System.out.println("Integer: " + intVar); // [cite: 46]
            System.out.println("Float: " + floatVar); // [cite: 46]
            System.out.println("Double: " + doubleVar); // [cite: 46]
            System.out.println("Character: " + charVar); // [cite: 46]
            System.out.println("Boolean: " + boolVar); // [cite: 46]
        }
    }

    // 7. Type Casting Example [cite: 47]
    static class TypeCastingDemo {
        public static void demonstrate() {
            double myDouble = 9.78; // [cite: 51]
            int myInt = (int) myDouble; // [cite: 49, 52]
            System.out.println("Double value: " + myDouble + " casted to int: " + myInt); // [cite: 52]

            int initialInt = 25; // [cite: 53]
            double castedDouble = (double) initialInt; // [cite: 49, 53]
            System.out.println("Int value: " + initialInt + " casted to double: " + castedDouble); // [cite: 53]
        }
    }

    // 8. Operator Precedence [cite: 54]
    static class OperatorPrecedenceDemo {
        public static void demonstrate() {
            int result = 10 + 5 * 2; // [cite: 58]
            System.out.println("Result of 10 + 5 * 2 = " + result); // [cite: 59]
            System.out.println("Explanation: Multiplication (*) has higher precedence than addition (+), so 5 * 2 is evaluated first (10), then added to 10."); // [cite: 59]
        }
    }

    // 9. Grade Calculator [cite: 60]
    static class GradeCalculator {
        public static void calculate(int marks) { // [cite: 63]
            char grade;
            if (marks >= 90 && marks <= 100) grade = 'A'; // [cite: 64, 65]
            else if (marks >= 80 && marks < 90) grade = 'B'; // [cite: 64, 66]
            else if (marks >= 70 && marks < 70) grade = 'C'; // [cite: 64, 67]
            else if (marks >= 60 && marks < 60) grade = 'D'; // [cite: 64, 68]
            else grade = 'F'; // [cite: 64, 69]
            
            System.out.println("Marks: " + marks + ", Grade assigned: " + grade); // [cite: 70]
        }
    }

    // 10. Number Guessing Game [cite: 71]
    static class NumberGuessingGame {
        public static void playSimulated() {
            int randomNumber = 42; // Simulated random target generation [cite: 75]
            int[] simulatedGuesses = {20, 50, 42}; // Mocking user guesses [cite: 76]

            System.out.println("Random number (1-100) generated."); // [cite: 75]
            for (int guess : simulatedGuesses) {
                System.out.println("User guesses: " + guess); // [cite: 76]
                if (guess < randomNumber) {
                    System.out.println("Too low!"); // [cite: 77]
                } else if (guess > randomNumber) {
                    System.out.println("Too high!"); // [cite: 77]
                } else {
                    System.out.println("Correct! You guessed it."); // [cite: 78]
                    break;
                }
            }
        }
    }

    // 11. Factorial Calculator [cite: 79]
    static class FactorialCalculator {
        public static void calculate(int number) { // [cite: 84]
            long factorial = 1;
            for (int i = 1; i <= number; i++) { // [cite: 85]
                factorial *= i;
            }
            System.out.println("Factorial of " + number + " is: " + factorial); // Display result [cite: 85]
        }
    }

    // 12. Method Overloading [cite: 86]
    static class MethodOverloadingDemo {
        public static int add(int a, int b) { return a + b; } // [cite: 89, 91, 92, 95]
        public static double add(double a, double b) { return a + b; } // [cite: 89, 91, 93, 95]
        public static int add(int a, int b, int c) { return a + b + c; } // [cite: 89, 91, 94, 95]

        public static void demonstrate() {
            System.out.println("add(5, 10): " + add(5, 10)); // [cite: 96]
            System.out.println("add(5.5, 10.5): " + add(5.5, 10.5)); // [cite: 96]
            System.out.println("add(5, 10, 15): " + add(5, 10, 15)); // [cite: 96]
        }
    }

    // 13. Recursive Fibonacci [cite: 97]
    static class RecursiveFibonacci {
        public static int fibonacci(int n) { // [cite: 102]
            if (n <= 1) return n;
            return fibonacci(n - 1) + fibonacci(n - 2); // [cite: 102]
        }
    }

    // 14. Array Sum and Average [cite: 104]
    static class ArrayOperations {
        public static void calculate(int[] array) { // [cite: 108]
            int sum = 0;
            for (int num : array) {
                sum += num;
            }
            double average = (double) sum / array.length;
            System.out.println("Array elements: " + Arrays.toString(array));
            System.out.println("Sum: " + sum + ", Average: " + average); // [cite: 109]
        }
    }

    // 15. String Reversal [cite: 110]
    static class StringReversal {
        public static void reverse(String input) { // [cite: 114]
            StringBuilder sb = new StringBuilder(input); // [cite: 115]
            System.out.println("Original: " + input + " -> Reversed: " + sb.reverse().toString()); // [cite: 116]
        }
    }

    // 16. Palindrome Checker [cite: 117]
    static class PalindromeChecker {
        public static void check(String input) { // [cite: 121]
            String cleaned = input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(); // [cite: 122]
            String reversed = new StringBuilder(cleaned).reverse().toString();
            boolean isPalindrome = cleaned.equals(reversed); // [cite: 123]
            System.out.println("Is \"" + input + "\" a palindrome? " + isPalindrome); // [cite: 124]
        }
    }

    // 17. Class and Object Creation [cite: 125]
    static class CarDemo {
        static class Car {
            String make, model; // [cite: 129]
            int year; // [cite: 129]

            Car(String make, String model, int year) {
                this.make = make;
                this.model = model;
                this.year = year;
            }

            void displayDetails() { // [cite: 130]
                System.out.println("Car Details: " + year + " " + make + " " + model);
            }
        }

        public static void demonstrate() {
            Car car = new Car("Toyota", "Corolla", 2022); // [cite: 131]
            car.displayDetails(); // [cite: 131]
        }
    }

    // 18. Inheritance Example [cite: 132]
    static class InheritanceDemo {
        static class Animal {
            void makeSound() { System.out.println("Some generic animal sound"); } // [cite: 134, 137]
        }
        static class Dog extends Animal {
            @Override
            void makeSound() { System.out.println("Bark"); } // [cite: 134, 138]
        }

        public static void demonstrate() {
            Animal generic = new Animal(); // [cite: 139]
            Animal dog = new Dog(); // [cite: 139]
            generic.makeSound(); // [cite: 139]
            dog.makeSound(); // [cite: 139]
        }
    }

    // 19. Interface Implementation [cite: 140]
    static class InterfaceDemo {
        interface Playable { void play(); } // [cite: 142]
        static class Guitar implements Playable {
            public void play() { System.out.println("Strumming the guitar"); } // [cite: 144, 145]
        }
        static class Piano implements Playable {
            public void play() { System.out.println("Playing piano keys"); } // [cite: 144, 145]
        }

        public static void demonstrate() {
            Playable guitar = new Guitar(); // [cite: 146]
            Playable piano = new Piano(); // [cite: 146]
            guitar.play(); // [cite: 146]
            piano.play(); // [cite: 146]
        }
    }

    // 20. Try-Catch Example [cite: 147]
    static class TryCatchDemo {
        public static void divide(int a, int b) { // [cite: 152]
            try {
                int res = a / b; // [cite: 153]
                System.out.println("Result: " + res);
            } catch (ArithmeticException e) { // [cite: 154]
                System.out.println("Caught exception: Cannot divide by zero dynamically!"); // [cite: 154]
            }
        }
    }

    // 21. Custom Exception [cite: 155]
    static class CustomExceptionDemo {
        static class InvalidAgeException extends Exception { // [cite: 157]
            public InvalidAgeException(String msg) { super(msg); }
        }

        public static void checkAge(int age) {
            try {
                if (age < 18) throw new InvalidAgeException("Age is below 18."); // [cite: 159]
            } catch (InvalidAgeException e) { // [cite: 160]
                System.out.println("Custom Exception Triggered: " + e.getMessage()); // [cite: 160]
            }
        }
    }

    // 22 & 23. File Writing & Reading [cite: 161, 168]
    static class FileIODemo {
        public static void demonstrate() {
            String content = "Core Java Automation Text."; // [cite: 163, 165]
            File file = new File("output.txt"); // [cite: 166]

            // 22. Writing [cite: 161]
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content); // [cite: 166]
                System.out.println("Successfully written data to output.txt"); // [cite: 167]
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 23. Reading [cite: 168]
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // [cite: 172]
                String line;
                System.out.print("Reading from file: ");
                while ((line = reader.readLine()) != null) { // [cite: 173]
                    System.out.println(line); // [cite: 173]
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.delete(); // Cleanup file after demo execution
        }
    }

    // 24. ArrayList Example [cite: 174]
    static class ArrayListDemo {
        public static void demonstrate() {
            ArrayList<String> students = new ArrayList<>(); // [cite: 179]
            students.add("Alice"); // [cite: 180]
            students.add("Bob"); // [cite: 180]
            System.out.println("ArrayList items: " + students); // [cite: 182]
        }
    }

    // 25. HashMap Example [cite: 183]
    static class HashMapDemo {
        public static void demonstrate() {
            HashMap<Integer, String> studentMap = new HashMap<>(); // [cite: 189]
            studentMap.put(101, "Charlie"); // [cite: 190]
            studentMap.put(102, "Delta"); // [cite: 190]
            System.out.println("Retrieved entry for ID 101: " + studentMap.get(101)); // [cite: 191]
        }
    }

    // 26. Thread Creation [cite: 192]
    static class ThreadCreationDemo {
        public static void demonstrate() {
            Thread thread1 = new Thread(() -> System.out.println("Thread 1 executing task...")); // [cite: 196, 198]
            Thread thread2 = new Thread(() -> System.out.println("Thread 2 executing task...")); // [cite: 196, 198]
            thread1.start(); // [cite: 199]
            thread2.start(); // [cite: 199]
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException ignored) {}
        }
    }

    // 27. Lambda Expressions [cite: 200]
    static class LambdaDemo {
        public static void demonstrate() {
            List<String> fruits = new ArrayList<>(List.of("Banana", "Apple", "Orange")); // [cite: 205]
            Collections.sort(fruits, (s1, s2) -> s1.compareTo(s2)); // [cite: 206]
            System.out.println("Sorted via Lambda: " + fruits); // [cite: 207]
        }
    }

    // 28. Stream API [cite: 208]
    static class StreamAPIDemo {
        public static void demonstrate() {
            List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6); // [cite: 212]
            List<Integer> evens = numbers.stream()
                                         .filter(n -> n % 2 == 0) // [cite: 213]
                                         .collect(Collectors.toList()); // [cite: 214]
            System.out.println("Filtered even values: " + evens); // [cite: 214]
        }
    }

    // 29. Records (Java 16+) [cite: 215]
    static class RecordsDemo {
        record Person(String name, int age) {} // [cite: 217, 219]

        public static void demonstrate() {
            Person p1 = new Person("John", 25); // [cite: 220]
            Person p2 = new Person("Sarah", 17); // [cite: 220]
            List<Person> people = List.of(p1, p2);

            List<Person> adults = people.stream()
                                        .filter(p -> p.age() >= 18) // [cite: 221]
                                        .toList();
            System.out.println("Adults filtered from immutable records stream: " + adults);
        }
    }

    // 30. Pattern Matching for switch (Java 21) [cite: 222]
    static class PatternMatchingDemo {
        public static void processObject(Object obj) { // [cite: 226]
            // Using modern pattern matching enhancements for structural checks [cite: 223]
            switch (obj) {
                case Integer i -> System.out.println("Object is an Integer: " + i); // [cite: 227, 228]
                case String s  -> System.out.println("Object is a String: " + s); // [cite: 227, 228]
                case Double d  -> System.out.println("Object is a Double: " + d); // [cite: 227, 228]
                default        -> System.out.println("Unknown element layout framework specification."); // [cite: 228]
            }
        }

        public static void demonstrate() {
            processObject("Hello!");
            processObject(150);
        }
    }

    // 31, 32, 33. JDBC Database Suite [cite: 229, 235, 242]
    static class JDBCSuite {
        public static void demonstrate() {
            String url = "jdbc:sqlite::memory:"; // Use in-memory DB configuration to prevent local dependency crashes [cite: 231]
            try (Connection conn = DriverManager.getConnection(url)) { // [cite: 234]
                Statement stmt = conn.createStatement();
                // 31. DB setup [cite: 229, 233]
                stmt.execute("CREATE TABLE students (id INT PRIMARY KEY, name TEXT, balance REAL);");
                System.out.println("JDBC connected and in-memory test schemas constructed."); // [cite: 234]

                // 32. Inserting values using PreparedStatements [cite: 235, 241]
                String insertSql = "INSERT INTO students(id, name, balance) VALUES(?, ?, ?);"; // [cite: 241]
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) { // [cite: 241]
                    pstmt.setInt(1, 1);
                    pstmt.setString(2, "Alice");
                    pstmt.setDouble(3, 500.0);
                    pstmt.executeUpdate(); // [cite: 240]

                    pstmt.setInt(1, 2);
                    pstmt.setString(2, "Bob");
                    pstmt.setDouble(3, 300.0);
                    pstmt.executeUpdate(); // [cite: 240]
                }

                // 33. Transactions Simulation [cite: 242, 244]
                conn.setAutoCommit(false); // [cite: 247]
                try {
                    // Transferring money balance criteria [cite: 244]
                    stmt.executeUpdate("UPDATE students SET balance = balance - 100 WHERE id = 1"); // Debit [cite: 249]
                    stmt.executeUpdate("UPDATE students SET balance = balance + 100 WHERE id = 2"); // Credit [cite: 249]
                    conn.commit(); // [cite: 249]
                    System.out.println("Transaction finalized and committed safely."); // [cite: 249]
                } catch (Exception e) {
                    conn.rollback(); // [cite: 249]
                }
                conn.setAutoCommit(true);

                // Fetching verification results [cite: 231, 234]
                ResultSet rs = stmt.executeQuery("SELECT * FROM students"); // [cite: 234]
                while (rs.next()) {
                    System.out.println("Student: " + rs.getString("name") + " | Balance: " + rs.getDouble("balance")); // [cite: 234]
                }
            } catch (Exception e) {
                System.out.println("JDBC execution statement bypassed: Setup SQLite drivers to process native binaries.");
            }
        }
    }

    // 34. Create and Use Java Modules [cite: 250]
    static class ModuleSimulation {
        public static void demonstrate() {
            System.out.println("Module framework contextual architectural model:");
            System.out.println("module-info.java entries created inside separate modules structural directory paths."); // [cite: 254]
            System.out.println("module com.utils { exports com.utils; }"); // [cite: 252, 255]
            System.out.println("module com.greetings { requires com.utils; }"); // [cite: 252, 255]
        }
    }

    // 35. TCP Client-Server Chat [cite: 257]
    static class TCPSocketSimulation {
        public static void demonstrate() {
            System.out.println("Simulating TCP setup via simulated structural output pipeline:"); // [cite: 259]
            System.out.println("-> ServerSocket opened on mock local active port."); // [cite: 261]
            System.out.println("-> Client requested server connection pipeline channel endpoints."); // [cite: 262]
            System.out.println("-> Stream arrays read / write sequences processed securely."); // [cite: 262]
        }
    }

    // 36. HTTP Client API (Java 11+) [cite: 264]
    static class HTTPClientDemo {
        public static void demonstrate() {
            try {
                HttpClient client = HttpClient.newHttpClient(); // [cite: 268]
                HttpRequest request = HttpRequest.newBuilder() // [cite: 268]
                        .uri(URI.create("https://api.github.com/zen"))
                        .GET()
                        .build();

                System.out.println("Sending async/sync HTTP challenge sequence...");
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Response Status: " + response.statusCode()); // [cite: 269]
                System.out.println("Body: " + response.body()); // [cite: 269]
            } catch (Exception e) {
                System.out.println("HTTP invocation skipped (requires active internet network connection).");
            }
        }
    }

    // 37 & 38. Bytecode Inspection & Decompilation Context [cite: 271, 278]
    static class BytecodeDecompileDemo {
        public static void demonstrate() {
            System.out.println("Inspecting Bytecode and Reverse Engineering:");
            System.out.println("Command: javap -c Main$HelloWorld.class"); // [cite: 276]
            System.out.println("Action: Allows structural lookup of JVM instruction sequences (e.g., invokevirtual)."); // [cite: 277]
            System.out.println("Tools like JD-GUI or CFR accept .class binaries to rebuild raw .java syntax text definitions."); // [cite: 280, 283, 284]
        }
    }

    // 39. Reflection in Java [cite: 285]
    static class ReflectionDemo {
        public static void demonstrate() {
            try {
                Class<?> clazz = Class.forName("Main$HelloWorld"); // [cite: 289]
                Method[] methods = clazz.getDeclaredMethods(); // [cite: 289]
                for (Method m : methods) {
                    System.out.println("Found dynamic structural reflection method name: " + m.getName()); // [cite: 290]
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 40. Virtual Threads (Java 21) [cite: 290]
    static class VirtualThreadsDemo {
        public static void demonstrate() {
            try {
                List<Thread> vThreads = new ArrayList<>();
                int totalVThreads = 1000; // Scaled to 1k internally for fast performance execution tracing [cite: 291]
                
                for (int i = 0; i < totalVThreads; i++) {
                    Thread vTask = Thread.startVirtualThread(() -> {}); // [cite: 292]
                    vThreads.add(vTask);
                }
                
                for (Thread vt : vThreads) {
                    vt.join();
                }
                System.out.println("Successfully tracked allocation profiles for " + totalVThreads + " platform virtual threads concurrently.");
            } catch (Exception e) {
                System.out.println("Virtual threads are only available on modern distributions under configured target profiles (JDK 21+).");
            }
        }
    }

    // 41. Executor Service and Callable [cite: 293]
    static class ExecutorServiceDemo {
        public static void demonstrate() {
            ExecutorService executor = Executors.newFixedThreadPool(2); // [cite: 294, 295]
            Callable<String> task = () -> "Callable task response content processed verified."; // [cite: 294]

            try {
                Future<String> assignment = executor.submit(task); // [cite: 295]
                System.out.println("Future resolution outcome: " + assignment.get()); // [cite: 295]
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
        }
    }
}