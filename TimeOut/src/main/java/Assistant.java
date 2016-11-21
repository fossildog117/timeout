import com.google.common.eventbus.SubscriberExceptionContext;

import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Filter;

/**
 * Created by nathanliu on 16/07/2016.
 */
abstract class Assistant {

    public static final String Line = "--------------------------------------";

    private static LinkedList<Subject> SubjectList = new LinkedList<>();

    // Some comparator for binary searching through a subject list
    public static Comparator<Subject> s = new Comparator<Subject>() {
        @Override
        public int compare(Subject o1, Subject o2) {
            return o1.getSubjectName().compareTo(o2.getSubjectName());
        }
    };

    // Given a sentence the function will return the individual words
    // E.g.
    // GetWords("hello my name is nathan") => ["hello", "my", "name", "is", "nathan"]
    // GetWords("hello") => ["hello"]
    // GetWords("hello world") => ["hello", "world"]
    static ArrayList<String> GetWords(String line) {

        ArrayList<String> fields = new ArrayList<>();
        String temp = "";

        for (int  i = 0 ; i < line.length(); i++) {

            temp += line.charAt(i);

            if (line.charAt(i) == ' ' || i == line.length() - 1) {
                fields.add(FilterCharacters(temp));
                if (i == line.length() - 1) {
                    break;
                }
                temp = "";
            }
        }
        return fields;
    }

    // Given a senentence this function will break it down into a triangle
    // e.g.
    // Breaker("hello world") => ["hello world one",
    //                                  "world one",
    //                                        "one"]
    // Breaker("hello my name is nathan okay") => ["hello my name is nathan okay",
    //                                                   "my name is nathan okay",
    //                                                      "name is nathan okay",
    //                                                           "is nathan okay",
    //                                                              "nathan okay",
    //                                                                     "okay"]
    static ArrayList<String> Breaker(String line) {

        ArrayList<String> fields = new ArrayList<>();
        fields.add(line);

        int end = line.length();

        for (int i = 0; i < end; i++) {
            if (line.charAt(0) == ' ') {
                fields.add(line.substring(1));
            }
            line = line.substring(1);
        }

        return fields;

    }

    
    // Filters characters from certain characters
    // E.g.
    // FilterCharacters("hello, it is quite cold; I think.") => "helloitisquitecoldIthink"
    // FilterCharacters("bool:") => "bool"
    private static String FilterCharacters(String line) {
        String[] rchars = {";", ":", ".", ",", " "};
        for (String character : rchars) {
            line = line.replace(character, "");
        }
        return line;
    }

    // Given an input string x, CompareSubject(string) will return true if either GetWords() or Breaker() returns true
    // SubjectList = ["BIOLOGY DEPARTMENT", "CHEMISTRY", "INSTITUTE OF GEOGRAPHY", "MATH", "SCHOOL OF PHYSICS"]
    // CompareSubject("Biology") => true
    // CompareSubject("School Of Physics") => true
    // CompareSubject("hello") => false
    static boolean CompareSubject(String line) {

        for (String word : Assistant.GetWords(line)) {
            // Check if word matches a department name


            int index = Collections.binarySearch(SubjectList, new Subject(word, 1, 1), s);
            if (index >= 0) {

                Subject subject = SubjectList.get(index);
                double recalProbability = Math.pow(2.7183, -(subject.getTimeSinceLastRecall() / (subject.getRetentionStrength())));
                double r = new Random().nextDouble();

                if (r < recalProbability) {
                    System.out.println("Recal probability = " + recalProbability + " for word: " + subject.getSubjectName() + " retention strength: " + subject.getRetentionStrength());
                    subject.incrementMatches();
                    return true;

                }
            }
        }
        return false;
    }

    static void RemoveDuplicates() {

        ArrayList<Subject> newList = new ArrayList<>();

        for (Subject subject : SubjectList) {

            boolean alreadyHas = false;

            for (Subject sub : newList) {
                if (sub.getSubjectName().equals(subject.getSubjectName())) {
                    alreadyHas = true;
                }
            }

            if (!alreadyHas) {
                newList.add(subject);
            }

        }

        SubjectList = new LinkedList<>(newList);

    }

    static void PrintList() {

        RemoveDuplicates();

        Collections.sort(SubjectList, (o1, o2) -> o1.getSubjectName().compareTo(o2.getSubjectName()));

        try {

            PrintWriter writer = new PrintWriter(System.getProperty("user.dir") + "/output.txt", "UTF-8");

            for (Subject s : SubjectList) {
                if (s.getRetentionStrength() >= 20) {
                    writer.println(s.getSubjectName().toUpperCase() + "," + s.getTimeSinceLastRecall() + "," + s.getRetentionStrength());
                }
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static String getEnd(String line) {

        if (line.charAt(line.length() - 1) == ',') {
            return "";
        } else {
            return getEnd(line.substring(0, line.length() - 1)) + line.charAt(line.length() - 1);
        }

    }

    static String getMid(String line) {

        String retString = "";

        for (int i = 0; i < line.length(); i++) {

            if (line.charAt(i) == ',') {

                for (int j = i + 1; j < line.length(); j++) {

                    if (line.charAt(j) == ',') {
                        break;
                    } else {
                        retString += line.charAt(j);
                    }

                }

                break;

            }

        }

        return retString;

    }

    static String getFirst(String line) {

        if (line.charAt(0) == ',') {
            return "";
        } else {
            return line.charAt(0) + getFirst(line.substring(1));
        }

    }

    public static LinkedList<Subject> getSubjectList() {
        return SubjectList;
    }
}

