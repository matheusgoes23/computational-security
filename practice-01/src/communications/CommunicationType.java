package communications;

public class CommunicationType {
    private static Communication communication;

    public static Communication getCommunication() {
        return communication;
    }

    public static void setCommunication(Communication communication) {
        CommunicationType.communication = communication;
    }
}