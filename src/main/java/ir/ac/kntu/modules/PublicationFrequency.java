package ir.ac.kntu.modules;

public enum PublicationFrequency {
    WEEKLY, MONTHLY, QUARTERLY;

    public static PublicationFrequency fromString(String input) {
        for (PublicationFrequency frequency : values()) {
            if (frequency.name().equalsIgnoreCase(input)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("Invalid frequency");
    }
}
