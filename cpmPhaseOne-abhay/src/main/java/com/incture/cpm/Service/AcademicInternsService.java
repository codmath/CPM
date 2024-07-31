package com.incture.cpm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.AcademicInterns;
import com.incture.cpm.Repo.AcademicInternsRepository;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
 
@Service
public class AcademicInternsService {
   @Autowired
    private AcademicInternsRepository attendanceRepository;
    private String date;
 
    public void readCSV(MultipartFile file) throws IOException {
 
        List<String[]> records = new ArrayList<>();
        String meetingTitle = "";
        String meetingDuration = "";
        List<String[]> participants = new ArrayList<>();
        String[] keywords = { "Organizer", "Presenter" };
 
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
 
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
 
            for (CSVRecord csvRecord : csvRecords) {
                List<String> values = new ArrayList<>();
                csvRecord.iterator().forEachRemaining(values::add);
                records.add(values.toArray(new String[0]));
            }
        }
 
        boolean participantSection = false;
        for (String[] record : records) {
            // Skip blank lines
            if (record.length == 0 || (record.length == 1 && record[0].trim().isEmpty())) {
                continue;
            }
            System.out.println(record.length);
            // Check for Meeting Title and Meeting Duration
            if (record.length == 1) {
                String header = record[0].trim();
                header = header.replace("\0", "");
                System.out.println("header->" + header + " (length: " + header.length() + ")");
 
                if (header.startsWith("Meeting title")) {
                    meetingTitle = header.split("\\t")[1].trim();
                    System.out.println("Meeting Title: " + meetingTitle);
                } else if (header.startsWith("Meeting duration")) {
                    meetingDuration = header.split("\\t")[1].trim();
                    System.out.println("Meeting Duration: " + meetingDuration);
                } else if (header.startsWith("2.")) {
                    participantSection = true;
                    System.out.println("Entering participant section.");
                } else if (header.equals("3. In-Meeting Activities")) {
                    participantSection = false;
                    System.out.println("Exiting participant section.");
                }
 
                continue;
            } else if (record.length == 2) {
                String header = record[0].trim();
                header = header.replace("\0", "");
                if (!header.contains("\"")) {
                    // Define the date pattern. This pattern matches dates in the format M/dd/yy or
                    // MM/dd/yy.
                    String datePattern = "\\b\\d{1,2}/\\d{1,2}/\\d{2}\\b";
                    Pattern pattern = Pattern.compile(datePattern);
                    Matcher matcher = pattern.matcher(header);
 
                    // If a date is found, add a quote before it
                    if (matcher.find()) {
                        int dateStartIndex = matcher.start();
                        header = new StringBuilder(header)
                                .insert(dateStartIndex, "\"")
                                .toString();
                    }
                }
                System.out.println("header2->" + header + " (length: " + header.length() + ")");
                date = header.substring(header.indexOf("\""), header.length());
            }
 
            // Extract participants if in participant section
            if (participantSection) {
                participants.add(record);
            }
        }
 
        // Debug prints
        System.out.println("Meeting Title: " + meetingTitle);
        System.out.println("Meeting Duration: " + meetingDuration);
 
        for (String[] participantData : participants) {
            System.out.println("Participant Raw Data: " + String.join(", ", participantData));
 
            // Create a new Attendance object for each participant
            AcademicInterns pp = new AcademicInterns();
            String role = null;
 
            // Set meeting title and duration
            meetingTitle = meetingTitle.replace("\0", "");
            pp.setMeetingTitle(meetingTitle);
            pp.setMeetingDuration(meetingDuration);
            date = date.substring(date.indexOf("\"") + 1);
            date = formatDate(date);
            pp.setDate(date);
 
            String fullName = null;
            String inMeetingDuration = null;
            String email = null;
 
            for (int j = 0; j < participantData.length; j++) {
                String data = participantData[j];
                data = data.replace("\0", "");
                System.out.print("Participant Data: " + data + " -- ");
 
                if (j == 0) {
                    if (!data.contains("\"")) {
                        System.out.println("entered");
                        // Define the date pattern. This pattern matches dates in the format M/dd/yy or
                        // MM/dd/yy.
                        String datePattern = "\\b\\d{1,2}/\\d{1,2}/\\d{2}\\b";
                        Pattern pattern = Pattern.compile(datePattern);
                        Matcher matcher = pattern.matcher(data);
 
                        // Debugging: Print the input string
                        System.out.println("Input: " + data);
 
                        // If a date is found, add a quote before it
                        if (matcher.find()) {
                            int dateStartIndex = matcher.start();
                            data = new StringBuilder(data)
                                    .insert(dateStartIndex, "\"")
                                    .toString();
                        }
 
                        // Debugging: Print the modified string
                        System.out.println("Modified: " + data);
                    }
                    System.out.println("header3->" + data + " (length: " + data.length() + ")");
                    // Extract and set full name
                    fullName = data.substring(0, data.indexOf("\"")).trim();
                    fullName = fullName.replace("\0", "");
                    pp.setFullName(fullName);
                } else if (j == 2) {
                    if (!data.contains("\"")) {
                        String timePattern = "\\b\\d{1,2}:\\d{2}:\\d{2} [AP]M\\b";
                        Pattern pattern = Pattern.compile(timePattern);
                        Matcher matcher = pattern.matcher(data);
 
                        // If a time is found, add a quote after it
                        if (matcher.find()) {
                            int timeEndIndex = matcher.end();
                            data = new StringBuilder(data)
                                    .insert(timeEndIndex, "\"")
                                    .toString();
                        }
                    }
                    // Extract and set in meeting duration
                    inMeetingDuration = data.substring(data.indexOf("\""), data.indexOf("s") + 1).trim();
                    inMeetingDuration = inMeetingDuration.replace("\0", "");
                    pp.setInMeetingDuration(inMeetingDuration.substring(inMeetingDuration.indexOf("\"") + 1));
 
                    // Extract role if present
                    data = data.replace("\0", "");
                    for (String keyword : keywords) {
                        int startIndex = data.indexOf(keyword);
                        if (startIndex != -1) {
                            role = data.substring(startIndex, startIndex + keyword.length());
                            break;
                        }
                    }
                    pp.setRole(role);
                    int emailStartIndex = data.indexOf("s") + 1;
                    int emailEndIndex = data.indexOf(".com") + 4; // Find the space after the email
                    if (emailStartIndex != -1) {
 
                        email = data.substring(emailStartIndex, emailEndIndex).trim();
                        pp.setEmail(email);
                    }
                } else if (j == 3) {
                    // Extract and set email
                    email = data.trim();
                    pp.setEmail(email);
                }
            }
             // Set status of 15% check
             System.out.println("Before -> In-Meeting Duration: " + inMeetingDuration + ", Meeting Duration: " + meetingDuration);
             inMeetingDuration = inMeetingDuration.substring(inMeetingDuration.indexOf("\"")+1).trim();
             int inMeetingSeconds = parseDuration(inMeetingDuration);
             int meetingSeconds = parseDuration(meetingDuration);
 
             // Calculate 15% of the meeting duration
             double threshold = meetingSeconds * 0.15;
             System.out.println("In-Meeting Seconds: " + inMeetingSeconds + ", Meeting Seconds: " + meetingSeconds);
             // Determine status based on the threshold
             if (inMeetingSeconds < threshold) {
                 pp.setStatus("absent");
             } else {
                 pp.setStatus("present");
             }
            // Save the populated Attendance object
            attendanceRepository.save(pp);
            System.out.println();
        }
 
        // return participantsArray;
    }
 
    private static int parseDuration(String duration) {
        Pattern pattern = Pattern.compile("(\\d+h)?\\s*(\\d+m)?\\s*(\\d+s)?");
        Matcher matcher = pattern.matcher(duration);
 
        int totalSeconds = 0;
        if (matcher.find()) {
            String hours = matcher.group(1);
            String minutes = matcher.group(2);
            String seconds = matcher.group(3);
 
            if (hours != null) {
                totalSeconds += Integer.parseInt(hours.replace("h", "")) * 3600;
            }
            if (minutes != null) {
                totalSeconds += Integer.parseInt(minutes.replace("m", "")) * 60;
            }
            if (seconds != null) {
                totalSeconds += Integer.parseInt(seconds.replace("s", ""));
            }
        }
        return totalSeconds;
    }
 
    private String formatDate(String date) {
        String[] dateParts = date.split("/");
        if (dateParts.length == 3) {
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);
   
            // Check if year part is 2 digits or 4 digits
            if (year < 100) {
                year += 2000; // Assuming all 2 digit years are in the 2000s
            }
           
            return "%02d/%02d/%04d".formatted(day, month, year);
        }
   
        return date;
    }
   
    public List<AcademicInterns> getAllAttendanceRecords() {
        return attendanceRepository.findAll();
    }
 
    public Optional<List<AcademicInterns>> getAttendanceByDate(String date) {
        return attendanceRepository.findByDate(date);
    }
 
    public AcademicInterns updateAttendance(Long id, AcademicInterns attendance) {
        attendance.setId(id);
        return attendanceRepository.save(attendance);
    }
 
    public Optional<AcademicInterns> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }
 
    public AcademicInterns addAttendanceManually(AcademicInterns attendance) {
        AcademicInterns saved = attendanceRepository.save(attendance);
        return saved;
    }    
}