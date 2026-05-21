import java.awt.*;
import java.util.concurrent.Semaphore;
import javax.swing.*; 
 
public class DiningPhilosophersGUI{ 
 
    static int N; 
    static Philosopher[] philosophers; 
    static Semaphore[] forks; 
    static JTextArea outputArea;     
    static String[] state; 
 
    // Lock for correct printing order 
    static final Object lock = new Object(); 
 
    // control flag for stopping     
    static volatile boolean running = true; 
 
    static class Philosopher extends Thread 
    {         
        int id; 
        Philosopher(int id) {             
            this.id = id; 
        } 
        public void run() 
        {             
            try { 
                while (running) {   //  loop added for continuous execution                     
                think();    
                eat(); 
                } 
            } 
            catch (InterruptedException e) 
            {                 
                e.printStackTrace(); 
            } 
        } 
        void think() throws InterruptedException
        {             
            log("Philosopher " + id + " is THINKING"); 
            Thread.sleep(500); 
        } 
        void eat() throws InterruptedException 
        {             
            updateState("HUNGRY"); 
 
            int left = id; 
            int right = (id + 1) % N; 
 
            log("P" + id + " tries to pick LEFT fork " + left); 
 
            // Deadlock prevention        
            if (id % 2 == 0)
            {          
                forks[left].acquire();        
                log("P" + id + " picked LEFT fork " + left); 
                log("P" + id + " tries RIGHT fork " + right);                 
                forks[right].acquire();      
                log("P" + id + " picked RIGHT fork " + right); 
            }
            else 
            { 
                forks[right].acquire();         
                log("P" + id + " picked RIGHT fork " + right); 
                log("P" + id + " tries LEFT fork " + left);                 
                forks[left].acquire();       
                log("P" + id + " picked LEFT fork " + left); 
            } 
            updateState("EATING"); 
 
            Thread.sleep(1000); 
 
            forks[left].release(); 
            forks[right].release(); 
 
            log("P" + id + " released forks " + left + " and " + right); 
 
            //  changed from FINISHED → THINKING 
            updateState("THINKING"); 
        } 
 
        void updateState(String newState) 
        {             
            synchronized (lock) 
            {   
                state[id] = newState; 
                log("Philosopher " + id + " -> " + newState); 
                printTable(); 
            } 
        } 
 
        void log(String message)
        {             
            synchronized (lock)
            {                 
                outputArea.append(message + "\n"); 
                try 
                { 
                    Thread.sleep(150); 
                } 
                catch (Exception e) {
                } 
            } 
        } 
    } 
 
    static void printTable() 
    { 
        StringBuilder sb = new StringBuilder("Current Status:\n"); 
        for (int i = 0; i < N; i++) 
        {      
       sb.append("P").append(i).append(": ").append(state[i]).append(" | "); 
        } 
        sb.append("\n---------------------------\n"); 
        outputArea.append(sb.toString()); 
    } 
 
    public static void main(String[] args) 
    { 
        JFrame frame = new JFrame("Dining Philosophers - FINAL CORRECT");         
        frame.setSize(700, 500);        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        JPanel panel = new JPanel(); 
        JTextField input = new JTextField(5); 
        JButton start = new JButton("Start Simulation"); 
        //  STOP BUTTON ADDED 
        JButton stop = new JButton("Stop"); 
        outputArea = new JTextArea(); 
        outputArea.setEditable(false); 
        panel.add(new JLabel("Enter Number of Philosophers:"));         
        panel.add(input);   
        panel.add(start);      
        panel.add(stop); //  added to GUI 
        start.addActionListener(e -> 
            { 
                try 
                { 
                    N = Integer.parseInt(input.getText()); 
                    if (N < 2 || N > 10) 
                    { 
                        JOptionPane.showMessageDialog(frame, "Enter 2–10");                     
                        return; 
                    } 
                    forks = new Semaphore[N];                  
                    state = new String[N]; 
                    philosophers = new Philosopher[N]; 
                    for (int i = 0; i < N; i++) 
                    {                     
                        forks[i] = new Semaphore(1); 
                        state[i] = "THINKING"; 
                    } 
                    printTable();     
                    running = true; // ensure running 
                    for (int i = 0; i < N; i++) 
                    {                     
                        philosophers[i] = new Philosopher(i); 
                        philosophers[i].start(); 
                    } 
                    start.setEnabled(false); 
                } 
                catch (Exception ex) 
                { 
                    JOptionPane.showMessageDialog(frame, "Invalid input"); 
                } 
            }
); 
 
        //  STOP LOGIC         
        stop.addActionListener(e -> 
        { 
            running = false;             
            outputArea.append("Simulation Stopped\n"); 
        }
); 
 
        frame.setLayout(new BorderLayout());   
        frame.add(panel, BorderLayout.NORTH);       
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER); 
        frame.setVisible(true); 
    } 
} 
