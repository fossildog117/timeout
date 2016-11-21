import java.util.Comparator;

/**
 * Created by nathanliu on 25/09/2016.
 */
public class Subject {

    private String subjectName;
    private int timeSinceLastRecall;
    private int retentionStrength;
    private int numberOfMatches;

    public Subject(String subjectName, int timeSinceLastRecall, int retentionStrength) {
        this.subjectName = subjectName;
        this.timeSinceLastRecall = timeSinceLastRecall;
        this.retentionStrength = retentionStrength;
        this.numberOfMatches = 0;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getTimeSinceLastRecall() {
        return timeSinceLastRecall;
    }

    public void incrementLastRecall() {
        this.timeSinceLastRecall++;
    }

    public void resetLastRecall() {
        this.timeSinceLastRecall = 1;
    }

    public int getRetentionStrength() {
        return retentionStrength;
    }

    public void incrementRetentionStrength() {
        this.retentionStrength += numberOfMatches;
    }

    public void incrementRetentionStrength(int val) {
        this.retentionStrength += val;
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public void incrementMatches() {
        numberOfMatches++;
    }

    public void resetNumberOfMatches() {
        this.numberOfMatches = 0;
    }
}
