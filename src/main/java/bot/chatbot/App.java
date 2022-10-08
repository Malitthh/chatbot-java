package bot.chatbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class App 
{
	//	start chatting from question and answer from t
	private static void startChatFromText() {
		
		HashMap<String,String> questionList = new HashMap<String, String>();
        try {
            File readFile = new File("question.txt");
            Scanner sc = new Scanner(readFile);
    
            while(sc.hasNextLine()){    
                String[] line = sc.nextLine().toLowerCase().trim().split("\\|");
                for(int i=0; i < line.length; i++)
                    questionList.put(line[0], line[1]);
            } 
            
            // System.out.println(questionList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Chat Bot. Type in some commands to get started!");
        
        while(true){
            System.out.print("Human:\t");
            String command = sc.nextLine().toLowerCase().trim();

            String reply ="Robot: \t";

            if(command.equalsIgnoreCase("exit")){
                reply = reply + " Will see you soon!";
                System.out.println(reply);
                System.exit(0);
            }else if(command.equalsIgnoreCase("check wheather today")){
            	String description = checkWeather();
            	reply = reply + "today's weather is " +description;
            	System.out.println(reply);
            }else if(questionList.containsKey(command)){
                String answer = questionList.get(command);
                reply = reply + answer;
                System.out.println(reply);
            }else{
                reply = reply + " Answer is not found in the existing system"; 
                System.out.println(reply);
                try {
                    saveDataTextFile(command);
                } catch (IOException e) { 
                    e.printStackTrace();
                }
            }
        }
	}
	
	private static boolean inArray(String in,String[] str){
        boolean match=false;
        for(int i=0;i<str.length;i++){
            if(str[i].equals(in)){
                match=true;
            }
        }
        return match;
    }
	
//	Method to hard-coded questions and answers
	private static void startChatRandomQA(){
        
		//String B[]= {"Good","Doing well","Not bad dear","I'm fine","I'm ok"};
		//Random r1= new Random();
		//int n = r1.nextInt(5);
		//System.out.println("Robot :" +B[n]);
		
		
		String[][] chatBot={
            //standard greetings
            {"Hi","hello","hola","ola","howdy"},
            {"hi","hello","hey"},
            // greetings
            {"how are you","how r you","how r u","how are u"},
            {"Good","Doing well","Not bad dear","I'm fine","I'm ok"},
            //thanking
            {"thank you","thanks"},
            {"You are welcome!"},
            //Fixed
            {"Good Morning","good morning"},
            {"Good Morning!"},
            
            {"What is your name","what is your name"},
            {"Travel Bot"},
            
            // questions
            {"what languages do sri lankans speak?","languages"},
            {"Sinhalese, Tamil, English"},
            {"What currency is used in Sri Lanka?","Currency"},
            {"Sri Lankan rupee (LKR)","LKR","The official and only currency used in the country is the Sri Lankan rupee (LKR)"},
            {"What is the climate like in Sri Lanka?","Climates"},
            {"The climate is tropical and warm, due to the moderating effects of ocean winds, which makes Sri Lanka such a popular beach holiday destination","Tropical and warm"},
            {"Top things to do in Sri Lanka?","Adventures in Sri Lanka?"},
            {"Climb Adams Peak, Enjoy the world's most beautiful train ride, Marvel at the Dambulla Cave Temples","Spot whales and dolphins in Mirissa, Hike Sigiriya Lion Rock Fortress, Check out the famous temples of Kandy, Spot the stilt fisherman in Galle"},
            //default
            {"Coudn't recognize the command", "Will get back to soon", "Not clear"}
        };
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Chat Bot. Type in some commands to get started!");
        
        while(true){
            System.out.print("Human:\t");
            String command = sc.nextLine();
            String saveQuestion = command;
            if(
                command.charAt(command.length()-1)=='!' ||
                command.charAt(command.length()-1)=='.' ||
                command.charAt(command.length()-1)=='?'
            ){
                command=command.substring(0,command.length()-1);
            }

            command = command.toLowerCase().trim();
            String reply ="Robot: \t";
            if(command.equalsIgnoreCase("exit")){
                reply = reply + " Will see you soon!";
                System.out.println(reply);
                System.exit(0);
            }else if(command.equalsIgnoreCase("check weather today")){
            	String description = checkWeather();
            	reply = reply + "today's weather is " +description;
            	System.out.println(reply);
            }else{
                byte response=0;
                int j=0;
                while(response==0){
                    if(inArray(command.toLowerCase(),chatBot[j*2])){
                        response=2;
                        int r=(int)Math.floor(Math.random()*chatBot[(j*2)+1].length);
                        reply = reply+chatBot[(j*2)+1][r];
                    }
                    j++;
                    if(j*2==chatBot.length-1 && response==0){
                        response=1;
                    }
                }
                //-----default--------------
                if(response==1){
                    int r=(int)Math.floor(Math.random()*chatBot[chatBot.length-1].length);
                    reply =reply+chatBot[chatBot.length-1][r];
                    try {
                        saveDataTextFile(saveQuestion);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(reply);
            }

        }
    }
	
	// Save unknown questions into cutomer.txt file	
	private static void saveDataTextFile(String question) throws IOException {
        System.out.println("Questions is data stored in text file");
        File readFile = new File("customer.txt");
        String questions = "";
        Scanner scanFile = new Scanner(readFile);
        while (scanFile.hasNextLine())
            questions =questions.trim() +"\n"+scanFile.nextLine();

        questions = questions +"\n"+question; 
          
        FileWriter fileWrite = new FileWriter("customer.txt");
        fileWrite.write(questions);
        fileWrite.close();
    }
	
//	Method to check wheather	
	private static String checkWeather() {
		String data = "";
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Colombo&appid=70033ec4fe0567aa834618d965265107");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();	

            // check if connected or not
            int responseCode = conn.getResponseCode();
            // 200: OK
            if(responseCode != 200){
                throw new RuntimeException("HttpResponseCode: "+ responseCode);
            }else{
              StringBuilder informationString = new StringBuilder();
              Scanner sc = new Scanner(url.openStream())  ;

              while(sc.hasNext()){
                  informationString.append(sc.nextLine());
              }

	          //   Close the scanner
	          sc.close();
//	          System.out.println(informationString);
	            
	          JSONParser parser = new JSONParser();
	          JSONObject json = (JSONObject) parser.parse(informationString.toString());
		      String t = json.get("weather").toString();
		      String txt = t.substring(1, t.length()-1);
		      JSONObject dataObject = (JSONObject) parser.parse(txt);
		      data = dataObject.get("description").toString();
	         
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
	}
    public static void main( String[] args )
    {
  	startChatRandomQA();
    	//startChatFromText();
    	
    }
}
