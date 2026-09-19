public class MobileDevice extends Device implements Connectable {
    private boolean connected;
    
    public MobileDevice(String name, int battery) {
        super(name, battery);
        connected = false;
    }
    
    @Override 
    public void connect() {
        connected = true;
    }
    
    @Override 
    public boolean isConnected() {
        return connected;
    }
    
    @Override 
    public String summary() {
        return super.summary() + " | Connected: " + connected;
    }
}