/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package judgeappointments;

import java.util.ArrayList;
import java.util.Scanner;

public class JudgeAppointments {

    // Superclass Judge
    static class Judge {
        private String name;
        private String id;
        private String specialization;
        private String[] availableDays;

        public Judge(String name, String id, String specialization, String[] availableDays) {
            this.name = name;
            this.id = id;
            this.specialization = specialization;
            this.availableDays = availableDays;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getSpecialization() {
            return specialization;
        }

        public String[] getAvailableDays() {
            return availableDays;
        }

        public void displayDetails() {
            System.out.println("Judge Name: " + name + ", Specialization: " + specialization);
            System.out.println("Available Days: " + String.join(", ", availableDays));
        }
    }

    // Appointment class
    static class Appointment {
        private String date;
        private String time;
        private Judge judge;
        private String caseType;

        public Appointment(String date, String time, Judge judge, String caseType) {
            this.date = date;
            this.time = time;
            this.judge = judge;
            this.caseType = caseType;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public Judge getJudge() {
            return judge;
        }

        public void displayAppointment() {
            System.out.println("Appointment Date: " + date + " " + time + ", Judge: " + judge.getName() + ", Case Type: " + caseType);
        }
    }

    static ArrayList<Judge> judges = new ArrayList<>();
    static ArrayList<Appointment> appointments = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Add initial data
        judges.add(new Judge("John Smith", "CJ001", "Civil", new String[]{"Monday", "Wednesday", "Friday"}));
        judges.add(new Judge("Emily Davis", "CJ002", "Criminal", new String[]{"Tuesday", "Thursday"}));
        judges.add(new Judge("Michael Johnson", "CJ003", "Family", new String[]{"Monday", "Thursday"}));

        int choice;
        do {
            System.out.println("\n--- Judge Appointment System ---");
            System.out.println("1. View All Judges");
            System.out.println("2. Search for Judge");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. View Appointments");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Reschedule Appointment");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    viewAllJudges();
                    break;
                case 2:
                    searchForJudge();
                    break;
                case 3:
                    scheduleAppointment();
                    break;
                case 4:
                    viewAppointments();
                    break;
                case 5:
                    cancelAppointment();
                    break;
                case 6:
                    rescheduleAppointment();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 7);
    }

    // View all judges
    public static void viewAllJudges() {
        System.out.println("\n--- All Judges ---");
        for (Judge judge : judges) {
            judge.displayDetails();
        }
    }

    // Search for a judge by name or specialization
    public static void searchForJudge() {
        System.out.print("Enter judge name or specialization: ");
        String query = scanner.nextLine();
        boolean found = false;
        for (Judge judge : judges) {
            if (judge.getName().equalsIgnoreCase(query) || judge.getSpecialization().equalsIgnoreCase(query)) {
                judge.displayDetails();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No judge found.");
        }
    }

    // Schedule an appointment
    public static void scheduleAppointment() {
        System.out.print("Enter appointment date (e.g., 2024-09-05): ");
        String date = scanner.nextLine();
        System.out.print("Enter appointment time (e.g., 10:00 AM): ");
        String time = scanner.nextLine();
        System.out.print("Enter case type: ");
        String caseType = scanner.nextLine();

        System.out.println("Suggested Judges for " + caseType + " cases:");
        for (int i = 0; i < judges.size(); i++) {
            if (judges.get(i).getSpecialization().equalsIgnoreCase(caseType)) {
                System.out.println((i + 1) + ". " + judges.get(i).getName() + " (" + judges.get(i).getSpecialization() + ")");
            }
        }

        System.out.print("Select judge by number: ");
        int judgeIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (judgeIndex >= 0 && judgeIndex < judges.size()) {
            Judge selectedJudge = judges.get(judgeIndex);

            // Check if judge is available
            if (!isJudgeAvailable(selectedJudge, date)) {
                System.out.println("Error: Judge is not available on the selected date.");
                return;
            }

            // Check for appointment conflicts
            for (Appointment appointment : appointments) {
                if (appointment.getJudge().equals(selectedJudge) && appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                    System.out.println("Error: Judge already has an appointment at this time on this date.");
                    return;
                }
            }

            appointments.add(new Appointment(date, time, selectedJudge, caseType));
            System.out.println("Appointment scheduled with Judge " + selectedJudge.getName());
        } else {
            System.out.println("Invalid judge selection.");
        }
    }

    // Check if the judge is available on the specified date
    public static boolean isJudgeAvailable(Judge judge, String date) {
        String dayOfWeek = getDayOfWeek(date); // Custom function to extract the day of the week from the date
        for (String day : judge.getAvailableDays()) {
            if (day.equalsIgnoreCase(dayOfWeek)) {
                return true;
            }
        }
        return false;
    }

    // Reschedule an appointment
    public static void rescheduleAppointment() {
        System.out.print("Enter the current appointment date to reschedule: ");
        String currentDate = scanner.nextLine();
        Appointment appointmentToReschedule = null;
        for (Appointment app : appointments) {
            if (app.getDate().equals(currentDate)) {
                appointmentToReschedule = app;
                break;
            }
        }
        if (appointmentToReschedule != null) {
            System.out.print("Enter new appointment date (e.g., 2024-09-10): ");
            String newDate = scanner.nextLine();
            System.out.print("Enter new appointment time (e.g., 11:00 AM): ");
            String newTime = scanner.nextLine();
            appointmentToReschedule.date = newDate;
            appointmentToReschedule.time = newTime;
            System.out.println("Appointment rescheduled.");
        } else {
            System.out.println("No appointment found on that date.");
        }
    }

    // View all appointments
    public static void viewAppointments() {
        System.out.println("\n--- All Appointments ---");
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            for (Appointment appointment : appointments) {
                appointment.displayAppointment();
            }
        }
    }

    // Cancel an appointment
    public static void cancelAppointment() {
        System.out.print("Enter appointment date to cancel: ");
        String date = scanner.nextLine();
        Appointment appointmentToCancel = null;
        for (Appointment app : appointments) {
            if (app.getDate().equals(date)) {
                appointmentToCancel = app;
                break;
            }
        }
        if (appointmentToCancel != null) {
            appointments.remove(appointmentToCancel);
            System.out.println("Appointment cancelled.");
        } else {
            System.out.println("No appointment found on that date.");
        }
    }

    // Utility method to extract the day of the week from a date
    public static String getDayOfWeek(String date) {
        // Mock function for the sake of simplicity. In real code, you'd use date parsing libraries.
        return "Monday"; // Replace with real day calculation based on the date string
    }
}