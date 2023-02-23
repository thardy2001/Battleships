import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;
import java.nio.charset.StandardCharsets;

import java.util.concurrent.TimeUnit;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.lang.Runnable;

public class Battleship
{

      private static volatile boolean connected = false;
      private static boolean session = false;

      private static int tcp_port;

      private static ServerSocket server_socket;
      private static Socket client_socket;

      private static PrintWriter tcp_out;
      private static BufferedReader tcp_in;

      private static Scanner sc = new Scanner(System.in);

      private static Random rand = new Random();

      private static class Board 
      {
            private String[] grid = new String[100]; // 10 x 10 grid

            private void Initilise()
            {

                  for(int i = 0; i < 100; i++){grid[i] = "~";}// initialize grid
            }

            private void PlaceCarrier()
            {
                  boolean placed = false;
                  int width = 10;
                  while(!placed)
                  {
                        int x = (int) (Math.random() * 99 ); // random coordinate
                        int direction = (int) (Math.random() * 4 ); // random rotation (0 = up, 1 = right, 2 = down, 3 = left)
                        if(direction == 0)//UP
                        {
                              
                              if(x - 4*width <0){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - width].equals("~") &&
                                    grid[x - 2*width].equals("~") &&
                                    grid[x - 3*width].equals("~") &&
                                    grid[x - 4*width].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="A"; 
                                    grid[x - width]="A";
                                    grid[x - 2*width]="A";
                                    grid[x - 3*width]="A";
                                    grid[x - 4*width]="A";   
                                    placed = true;                               
                              }
                        }
                        if(direction == 2)//DOWN
                        {
                              
                              if(x + 4*width >99){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + width].equals("~") &&
                                    grid[x + 2*width].equals("~") &&
                                    grid[x + 3*width].equals("~") &&
                                    grid[x + 4*width].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="A"; 
                                    grid[x + width]="A";
                                    grid[x + 2*width]="A";
                                    grid[x + 3*width]="A";
                                    grid[x + 4*width]="A";
                                    placed = true;                                   
                              }
                        }


                        if(direction == 1)//RIGHT
                        {
                              
                              if(x+4 > 99 || (x+4)%10<4){continue;} // Within the board and doesn't continue down to next row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + 1].equals("~") &&
                                    grid[x + 2].equals("~") &&
                                    grid[x + 3].equals("~") &&
                                    grid[x + 4].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="A"; 
                                    grid[x + 1]="A";
                                    grid[x + 2]="A";
                                    grid[x + 3]="A";
                                    grid[x + 4]="A"; 
                                    placed = true;                                  
                              }
                        }
                        if(direction == 3)//LEFT
                        {
                              
                              if(x-4 < 0 || (x-4)%10>5){continue;} // Within the board and doesn't continue up to previous row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - 1].equals("~") &&
                                    grid[x - 2].equals("~") &&
                                    grid[x - 3].equals("~") &&
                                    grid[x - 4].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="A"; 
                                    grid[x - 1]="A";
                                    grid[x - 2]="A";
                                    grid[x - 3]="A";
                                    grid[x - 4]="A"; 
                                    placed = true;                                  
                              }
                        }

                  }
            }

            private void PlaceBattleship()
            {
                  boolean placed = false;
                  int width = 10;
                  while(!placed)
                  {
                        int x = (int) (Math.random() * 99 ); // random coordinate
                        int direction = (int) (Math.random() * 4 ); // random rotation (0 = up, 1 = right, 2 = down, 3 = left)
                        if(direction == 0)//UP
                        {
                             
                              if(x - 3*width <0){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - width].equals("~") &&
                                    grid[x - 2*width].equals("~") &&
                                    grid[x - 3*width].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="B"; 
                                    grid[x - width]="B";
                                    grid[x - 2*width]="B";
                                    grid[x - 3*width]="B";   
                                    placed = true;                               
                              }
                        }
                        if(direction == 2)//DOWN
                        {
                              
                              if(x + 3*width >99){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + width].equals("~") &&
                                    grid[x + 2*width].equals("~") &&
                                    grid[x + 3*width].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="B"; 
                                    grid[x + width]="B";
                                    grid[x + 2*width]="B";
                                    grid[x + 3*width]="B";
                                    placed = true;                                   
                              }
                        }


                        if(direction == 1)//RIGHT
                        {
                              
                              if(x+3 > 99 || (x+3)%10<3){continue;} // Within the board and doen't continue down to next row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + 1].equals("~") &&
                                    grid[x + 2].equals("~") &&
                                    grid[x + 3].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="B"; 
                                    grid[x + 1]="B";
                                    grid[x + 2]="B";
                                    grid[x + 3]="B"; 
                                    placed = true;                                  
                              }
                        }
                        if(direction == 3)//LEFT
                        {
                              
                              if(x-3 < 0 || (x-3)%10>6){continue;} // Within the board and doen't continue up to previous row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - 1].equals("~") &&
                                    grid[x - 2].equals("~") &&
                                    grid[x - 3].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="B"; 
                                    grid[x - 1]="B";
                                    grid[x - 2]="B";
                                    grid[x - 3]="B"; 
                                    placed = true;                                  
                              }
                        }

                  }
            }

            private void PlaceSub()
            {
                  boolean placed = false;
                  int width = 10;
                  while(!placed)
                  {
                        int x = (int) (Math.random() * 99 ); // random coordinate
                        int direction = (int) (Math.random() * 4 ); // random rotation (0 = up, 1 = right, 2 = down, 3 = left)
                        if(direction == 0)//UP
                        {
                              
                              if(x - 2*width <0){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - width].equals("~") &&
                                    grid[x - 2*width].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="S"; 
                                    grid[x - width]="S";
                                    grid[x - 2*width]="S";  
                                    placed = true;                               
                              }
                        }
                        if(direction == 2)//DOWN
                        {
                              
                              if(x + 2*width >99){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + width].equals("~") &&
                                    grid[x + 2*width].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="S"; 
                                    grid[x + width]="S";
                                    grid[x + 2*width]="S";
                                    placed = true;                                   
                              }
                        }

                        if(direction == 1)//RIGHT
                        {
                              
                              if(x+2 > 99 || (x+2)%10<2){continue;} // Within the board and doen't continue down to next row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + 1].equals("~") &&
                                    grid[x + 2].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="S"; 
                                    grid[x + 1]="S";
                                    grid[x + 2]="S"; 
                                    placed = true;                                  
                              }
                        }
                        if(direction == 3)//LEFT
                        {
                             
                              if(x-2 < 0 || (x-2)%10>7){continue;} // Within the board and doen't continue up to previous row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - 1].equals("~") &&
                                    grid[x - 2].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="S"; 
                                    grid[x - 1]="S";
                                    grid[x - 2]="S"; 
                                    placed = true;                                  
                              }
                        }

                  }
            }
            private void PlaceCruiser()
            {
                  boolean placed = false;
                  int width = 10;
                  while(!placed)
                  {
                        int x = (int) (Math.random() * 99 ); // random coordinate
                        int direction = (int) (Math.random() * 4 ); // random rotation (0 = up, 1 = right, 2 = down, 3 = left)
                        if(direction == 0)//UP
                        {
                              
                              if(x - 2*width <0){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - width].equals("~") &&
                                    grid[x - 2*width].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="C"; 
                                    grid[x - width]="C";
                                    grid[x - 2*width]="C";  
                                    placed = true;                               
                              }
                        }
                        if(direction == 2)//DOWN
                        {
                              
                              if(x + 2*width >99){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + width].equals("~") &&
                                    grid[x + 2*width].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="C"; 
                                    grid[x + width]="C";
                                    grid[x + 2*width]="C";
                                    placed = true;                                   
                              }
                        }

                        if(direction == 1)//RIGHT
                        {
                              
                              if(x+2 > 99 || (x+2)%10<2){continue;} // Within the board and doen't continue down to next row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + 1].equals("~") &&
                                    grid[x + 2].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="C"; 
                                    grid[x + 1]="C";
                                    grid[x + 2]="C"; 
                                    placed = true;                                  
                              }
                        }
                        if(direction == 3)//LEFT
                        {
                             
                              if(x-2 < 0 || (x-2)%10>7){continue;} // Within the board and doen't continue up to previous row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - 1].equals("~") &&
                                    grid[x - 2].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="C"; 
                                    grid[x - 1]="C";
                                    grid[x - 2]="C"; 
                                    placed = true;                                  
                              }
                        }

                  }
            }
            private void PlacePatrolboat()
            {
                  boolean placed = false;
                  int width = 10;
                  while(!placed)
                  {
                        int x = (int) (Math.random() * 99 ); // random coordinate
                        int direction = (int) (Math.random() * 4 ); // random rotation (0 = up, 1 = right, 2 = down, 3 = left)
                        if(direction == 0)//UP
                        {
                              
                              if(x - 1*width <0){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - width].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="P"; 
                                    grid[x - width]="P"; 
                                    placed = true;                               
                              }
                        }
                        if(direction == 2)//DOWN
                        {
                              
                              if(x + 1*width >99){continue;}// If any of the sections of the ship are out of the grid then the last section will also be out. 
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + width].equals("~") 
                              )
                              { // Place the ship
                                    grid[x]="P"; 
                                    grid[x + width]="P";
                                    placed = true;                                   
                              }
                        }

                        if(direction == 1)//RIGHT
                        {
                              
                              if(x+1 > 99 || (x+1)%10==0){continue;} // Within the board and doen't continue down to next row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x + 1].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="P"; 
                                    grid[x + 1]="P"; 
                                    placed = true;                                  
                              }
                        }
                        if(direction == 3)//LEFT
                        {
                              
                              if(x-1 < 0 || (x-1)%10==9){continue;} // Within the board and doen't continue up to previous row
                              if
                              ( // Check that all cells above the random chosen cell are empty
                                    grid[x].equals("~") &&
                                    grid[x - 1].equals("~")
                              )
                              { // Place the ship
                                    grid[x]="P"; 
                                    grid[x - 1]="P"; 
                                    placed = true;                                  
                              }
                        }

                  }
            }
            private void PlaceShips()
            {
                  PlacePatrolboat();
                  PlaceCruiser();
                  PlaceSub();
                  PlaceCarrier();
                  PlaceBattleship();
            }
            private String GetImage()
            {
                  //                      Top of image
                  String image = "   ________________________________\n  /                                \\\n /                                  \\\n|   A  B  C  D  E  F  G  H  I  J     |\n";
                  
                  for(int i = 0; i < 9;i++)
                  {
                        
                        image+= "|" + (i + 1) + "  ";
                        for(int j = 0; j < 10; j++)
                        {
                              if(!grid[j + i*10].equals("~") && !grid[j + i*10].equals("#") && !grid[j + i*10].equals("X")){image+= "O" + "  ";}
                              else{image+= grid[j + i*10] + "  ";}
                        }
                        image+= " " + (i + 1) + " |\n";
                  }


                  // Do 10 seperate for allignment
                  image+= "|" + 10 + " ";
                  for(int j = 0; j < 10; j++)
                  {
                        if(!grid[j+90].equals("~") && !grid[j+90].equals("#") && !grid[j+90].equals("X")){image+= "O" + "  ";}
                        else{image+= grid[j + 90] + "  ";}
                  }
                  image+= "" + (10) + " |\n";
                  


                  //                Bottom of image
                  image+="\\   A  B  C  D  E  F  G  H  I  J     / \n \\__________________________________/\n";
                  return image; 
            }

            // Seperate coordinates and confirm validity. returns null on invalid input
            private String[] ParseCoordinates(String coordinate)
            {
                  String x = coordinate.substring(0,1).toUpperCase();
                  if(!"ABCDEFGHIJ".contains(x)){return null;}// invalid coordinate

                  String y = coordinate.substring(1);

                  try
                  {
                        if(Integer.parseInt(y) <1 || Integer.parseInt(y) >10){return null;} // invalid coordinate
                  }
                  catch(Exception e){return null;} // invalid coordinate

                  String[] parsed = {x,y};
                  return parsed;
            }
            // Convert coordinates to grid index
            private int GetIndex(String c) // c is values A1-J10
            {
                  String[] coords = ParseCoordinates(c);
                  
                  char letter = coords[0].charAt(0);
                  int letter_int = letter - 'A';
                  try
                  {
                  //           width      *     y-coord           +      x-coord   
                        return (10 * (Integer.parseInt(coords[1]) -1) ) + letter_int; // convert to 1-D array index
                  }
                  catch(Exception e){System.out.println("Failed to convert coordinates to index"); return -1;}
    
            }

            // Set value in grid 
            private void UpdateGrid(String coordinate, String value) // grid location, new value
            {

                  grid[GetIndex(coordinate)] = value;
            }

            // Return value in grid
            private String GetGridValue(String coordinate)
            {

                  return grid[GetIndex(coordinate)];
            }


      }
      private class Listen implements Runnable
      {
            private PrintWriter tcp_out;
            private BufferedReader tcp_in;

            public void run()
            {
                  try
                  {
                        server_socket = new ServerSocket(tcp_port);
                        client_socket = server_socket.accept();
                        tcp_in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
                        tcp_out = new PrintWriter(client_socket.getOutputStream(),true);                        
                  }
                  catch(Exception e)
                  {
                        System.out.println("Error establishing connection");
                        e.printStackTrace();
                  }

                  connected = true;
                  session = true; 

                  // Setup
                  Board client_board = new Board(); // to determine hits
                  Board opponent_board = new Board(); // for showing where client has already fired 

                  client_board.Initilise();
                  client_board.PlaceShips();
                  opponent_board.Initilise();

                  int carrier_health = 5;
                  int battleship_health = 4;
                  int sub_health = 3;
                  int cruiser_health = 3;
                  int patrol_health =2;
                  String notification = "";
                  System.out.println(opponent_board.GetImage() + "\n\n ------------------------------------\n\n" + client_board.GetImage());
                  while(session)
                  {

                        // RECEIVE ATTACK
                        System.out.println("Opponents Turn...");
                        String incoming ="";

                        // read from buffer
                        try{incoming = tcp_in.readLine();}catch(Exception e){e.printStackTrace();}
                        
                        String[] attack = incoming.split(":");
                        // Confirm received a 'FIRE' command
                        if(!attack[0].equals("FIRE")){System.out.println("Expected FIRE got '" + attack[0] + "'"); System.exit(0);}

                        String target = client_board.GetGridValue(attack[1]); // what was at the targeted location
                        String message = ""; // what will be sent to the opponent
                        notification = ""; // tell the client what their attack hit (or missed)

                        switch(target)
                        {
                              case "A" : // Aircraft Carrier
                                    carrier_health -= 1;
                                    // sunk
                                    if(carrier_health == 0){message ="SUNK:" + attack[1] + ":" + "Carrier [5]";notification = "They Sunk Your Carrier! [5]";}
                                    // hit
                                    else{message = "HIT:" + attack[1];notification = "They Hit Your Carrier! [5]";}  
                                    client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                    
                                    break;

                              case "B" : // Battleship
                                    battleship_health -= 1;
                                    // sunk
                                    if(battleship_health == 0){message = "SUNK:" + attack[1] + ":" + "Battleship [4]";notification = "They Sunk Your Battleship! [4]";}
                                    //hit
                                    else{message = "HIT:" + attack[1];notification = "They Hit Your Battleship! [4]";}
                                    client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                    
                                    break;

                              case "S": // Submarine
                                    sub_health -= 1;
                                    // sunk
                                    if(sub_health == 0){message = "SUNK:" + attack[1] + ":" + "Submarine [3]";notification = "They Sunk Your Submarine! [3]";}
                                    // hit
                                    else{message = "HIT:" + attack[1];notification = "They Hit Your Submarine! [3]";}
                                    client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                    
                                    break;

                              case "C": // Cruiser
                                    cruiser_health -= 1;
                                    // sunk
                                    if(cruiser_health == 0){message = "SUNK:" + attack[1] + ":" + "Cruiser [3]";notification = "They Sunk Your Cruiser! [3]";}
                                    // hit
                                    else{message = "HIT:" + attack[1];notification = "They Hit Your Cruiser! [3]";}
                                    client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                    
                                    break;

                              case "P" : // PATROL
                                    patrol_health -= 1;
                                    // sunk
                                    if(patrol_health == 0){message = "SUNK:" + attack[1] + ":" + "Patrol Boat [2]";notification = "They Sunk Your Patrol Boat! [2]";}
                                    //hit
                                    else{message = "HIT:" + attack[1];notification = "They Hit Your Patrol Boat! [2]";}
                                    client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                    
                                    break;

                              default: // MISS (on an X or ~ or #)
                                    message = "MISS:" + attack[1];
                                    client_board.UpdateGrid(attack[1], "#"); // Mark Miss
                                    notification = "They Missed!";
                        }
                        if(battleship_health + carrier_health + cruiser_health + sub_health + patrol_health == 0)
                        {
                              message = "GAME OVER" + message.substring(4); // override 'HIT' with 'GAME OVER'
                              tcp_out.println(message);
                              System.out.println("You Lost!");
                              try{Thread.sleep(5000);}catch(Exception e){System.exit(0);} // Graceful Exit
                              System.exit(0);
                        }
                        else{tcp_out.println(message);}
                        

                        
                        System.out.println(opponent_board.GetImage() + "\n\n ------------------------------------\n\n" + client_board.GetImage());
                        System.out.println(notification);
                        // ________________________________________________
                        // SEND AN ATTACK

                        
                        String command ="";
                        while(true) // breaks on valid command
                        {
                              System.out.print("Target: ");
                              try{command = sc.nextLine();}
                              catch(Exception e){System.out.println("ERROR reading input of target"); e.printStackTrace();}

                              String[] coords = client_board.ParseCoordinates(command);
                              if(coords == null){System.out.println("Invalid");continue;}
                              else
                              {
                                    tcp_out.println("FIRE:" + coords[0] + coords[1]);
                                    break;
                              }
                        } // WHILE getting attack

                        // ________________________________
                        // RECEIEVE RESULT OF OUR ATTACK

                        String in = "";
                        try{in = tcp_in.readLine();}catch(IOException e){System.out.println("FAILED TO READ result");e.printStackTrace();System.exit(0);}
                        String[] result = in.split(":");
                        notification = "";

                        switch(result[0])
                        {
                              case "MISS":
                                    notification = result[1] + " Was a Miss!";
                                    opponent_board.UpdateGrid(result[1], "#");
                                    break;
                              case "HIT":
                                    notification = result[1] + " Was a Hit!";
                                    opponent_board.UpdateGrid(result[1], "X");
                                    break;
                              case "SUNK":
                                    notification = result[1] + " You Sunk Their " + result[2];
                                    opponent_board.UpdateGrid(result[1], "X");
                                    break;
                              case "GAME OVER":
                                    session = false;
                                    System.out.println("You Win!");
                                    System.exit(0);
                                    break;
                              default:          // NOT A VALID NOTIICATION
                                    System.out.println(result[0] + " -- INVALID");
                                    break;
                        }
                        System.out.println(opponent_board.GetImage() + "\n\n ------------------------------------\n\n" + client_board.GetImage());
                        System.out.println(notification);
                  }// WHILE in Session

            } // RUNNABLE

      }// LISTEN

      //                     [0] broadcast address | [1] broadcast port
      public static void main(String[] args)
      {

            InetAddress broadcast_address = null;
            int broadcast_listen_port = 4444; 

            try
            {
                  broadcast_address = InetAddress.getByName(args[0]);
                  broadcast_listen_port = Integer.parseInt(args[1]);

            }
            catch(Exception e)
            {
                  System.out.println("Invalid Arguments");
                  e.printStackTrace();
                  System.exit(0);
            }


            Thread t1 = new Thread(new Battleship().new Listen());
            t1.start();

            int max = 5000;
            int min = 2000;
            tcp_port = (rand.nextInt(max - min - 1) + min);


            // Build UDP packet to broadcast
            String broadcast_message = "NEW PLAYER:" + String.valueOf(tcp_port);
            byte[] broadcast_bytes = broadcast_message.getBytes();
            DatagramPacket broadcast_packet = new DatagramPacket(broadcast_bytes, broadcast_bytes.length, broadcast_address, broadcast_listen_port);
            byte[] buff = new byte[15];

            DatagramPacket receive_packet = new DatagramPacket(buff, buff.length);

            DatagramSocket datagram_listen_socket = null;
            DatagramSocket datagram_broadcast_socket = null;

            while(true) // Find a port to broadcast from 
            {
                  try
                  {
                        datagram_broadcast_socket = new DatagramSocket((rand.nextInt(8000 - 2000) + 2000)); // random port to broadcast from 
                        // successful bind
                        datagram_broadcast_socket.setBroadcast(true);
                        break;
                  }
                  catch(Exception e)
                  {
                        // failed to bind to port 
                        continue;
                  }
            }


            while(!connected)
            {
                  try
                  {
                        for(int i = 0; i < (rand.nextInt(5) + 1); i++)// broadcast a random number of times 
                        {
                              datagram_broadcast_socket.send(broadcast_packet);
                              Thread.sleep(10); // wait so as not to receive our own packet
                        }

                        // attempt bind a random number of times
                        for(int i = 0; i < (rand.nextInt(5) + 1); i++)
                        {
                              try
                              {
                                    datagram_listen_socket = new DatagramSocket(broadcast_listen_port);
                                    // Successful bind
                                    datagram_listen_socket.setSoTimeout(30000); // Timeout 30sec
                                    datagram_listen_socket.receive(receive_packet);

                              } catch(Exception e)
                              {
                                    // failed to bind
                                    Thread.sleep(10); // wait before attempting again
                              }

                              // Process UDP packet
                              String received_string = new String(receive_packet.getData(),StandardCharsets.UTF_8); 

                              String[] received_data = received_string.split(":");

                              try
                              {
                                    if(received_data[0].equals("NEW PLAYER"))
                                    {
                                          // Attempt to create TCP connection
                                          try
                                          {
                                                client_socket = new Socket(receive_packet.getAddress(),Integer.parseInt(received_data[1]));

                                                tcp_in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
                                                tcp_out = new PrintWriter(client_socket.getOutputStream(),true);
                                          }catch(Exception e){System.out.println("Failed to create outbound TCP connection"); continue;}

     
                                          t1.stop(); //success in creating TCP kill the thread
                                          connected = true; 
                                          session = true;

                                          // Setup
                                          Board client_board = new Board(); // to determine hits
                                          Board opponent_board = new Board(); // for showing where client has already fired 

                                          client_board.Initilise();
                                          client_board.PlaceShips();
                                          opponent_board.Initilise();

                                          int battleship_health = 4;
                                          int carrier_health = 5;
                                          int sub_health = 3;
                                          int cruiser_health = 3;
                                          int patrol_health =2;

                                          String notification = "";
                                          
                                          System.out.println(opponent_board.GetImage() + "\n\n ------------------------------------\n\n" + client_board.GetImage());
                                          while(session)
                                          {
                                                // ________________________________________________
                                                // SEND AN ATTACK

                                                String command ="";
                                                while(true) // Break on transmission of valid command
                                                {
                                                      System.out.print("Target: ");
                                                      try{command = sc.nextLine();}
                                                      catch(Exception e){System.out.println("ERROR reading input of target"); e.printStackTrace();}

                                                      String[] coords = client_board.ParseCoordinates(command);
                                                      
                                                      if(coords == null){System.out.println("Invalid");continue;}
                                                      else
                                                      {
                                                            tcp_out.println("FIRE:" + coords[0] + coords[1]);
                                                            break;
                                                      }
                                                } // WHILE transmitting attack

                                                                        
                                                // ________________________________
                                                // RECEIEVE RESULT OF OUR ATTACK

                                                String in = "";
                                                try{in = tcp_in.readLine();}catch(IOException e){System.out.println("FAILED TO READ result");e.printStackTrace();System.exit(0);}
                                                String[] result = in.split(":");
                                                notification = "";

                                                
                                                switch(result[0])
                                                {
                                                      case "MISS":
                                                            notification = result[1] + " Was a Miss!";
                                                            opponent_board.UpdateGrid(result[1], "#");
                                                            break;
                                                      case "HIT":
                                                            notification = result[1] + " Was a Hit!";
                                                            opponent_board.UpdateGrid(result[1], "X");
                                                            break;
                                                      case "SUNK":
                                                            notification = result[1] + " You Sunk Their " + result[2];
                                                            opponent_board.UpdateGrid(result[1], "X");
                                                            break;
                                                      case "GAME OVER":
                                                            session = false;
                                                            System.out.println("You Win!");
                                                            System.exit(0);
                                                            break;
                                                      default:          // NOT A VALID NOTIICATION
                                                            System.out.println(result[0] + " -- INVALID");
                                                            break;
                                                }
                                                System.out.println(opponent_board.GetImage() + "\n\n ------------------------------------\n\n" + client_board.GetImage());
                                                System.out.println(notification);

                                                //_____________________________________________
                                                // Receive Attack

                                                System.out.println("Opponents Turn...");
                                                String incoming ="";
                                                try
                                                {
                                                      incoming = tcp_in.readLine();
                                                }catch(Exception e){e.printStackTrace();}
                                                
                                                String[] attack = incoming.split(":");
                                                // Confirm received a 'FIRE' command
                                                if(!attack[0].equals("FIRE")){System.out.println("Expected FIRE got '" + attack[0] + "'"); System.exit(0);}

                                                String target = client_board.GetGridValue(attack[1]); // what was at the targeted location
                                                String message = ""; // what will be sent to the opponent
                                                notification = ""; // Tell the client what their attack hit (or missed)

                                                switch(target)
                                                {
                                                      case "A" : // Aircraft Carrier
                                                            carrier_health -= 1;
                                                            // sunk
                                                            if(carrier_health == 0){message ="SUNK:" + attack[1] + ":" + "Carrier [5]";notification = "They Sunk Your Carrier! [5]";} 
                                                            // hit
                                                            else{message = "HIT:" + attack[1];notification = "They Hit Your Carrier! [5]";}  
                                                            client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                                            
                                                            break;

                                                      case "B" : // Battleship
                                                            battleship_health -= 1;
                                                            // sunk
                                                            if(battleship_health == 0){message = "SUNK:" + attack[1] + ":" + "Battleship [4]";notification = "They Sunk Your Battleship! [4]";}
                                                            // hit
                                                            else{message = "HIT:" + attack[1];notification = "They Hit Your Battleship! [4]";}
                                                            client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                                            
                                                            break;

                                                      case "S": // Submarine
                                                            sub_health -= 1;
                                                            // sunk
                                                            if(sub_health == 0){message = "SUNK:" + attack[1] + ":" + "Submarine [3]";notification = "They Sunk Your Submarine! [3]";}
                                                            // hit
                                                            else{message = "HIT:" + attack[1];notification = "They Hit Your Submarine! [3]";}
                                                            client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                                            
                                                            break;

                                                      case "C": // Cruiser
                                                            cruiser_health -= 1;
                                                            // sunk
                                                            if(cruiser_health == 0){message = "SUNK:" + attack[1] + ":" + "Cruiser [3]";notification = "They Sunk Your Cruiser! [3]";} 
                                                            // hit
                                                            else{message = "HIT:" + attack[1];notification = "They Hit Your Crusier! [3]";}
                                                            client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                                            
                                                            break;

                                                      case "P" : // PATROL
                                                            patrol_health -= 1;
                                                            // sunk
                                                            if(patrol_health == 0){message = "SUNK:" + attack[1] + ":" + "Patrol Boat [2]";notification = "They Sunk Your Patrol Boat! [2]";} 
                                                            // hit
                                                            else{message = "HIT:" + attack[1];notification = "They Hit Your Patrol Boat! [2]";}
                                                            client_board.UpdateGrid(attack[1], "X"); // Mark Hit
                                                            
                                                            break;

                                                      default: // MISS (is a X or ~ or #)
                                                            message = "MISS:" + attack[1];
                                                            client_board.UpdateGrid(attack[1], "#"); // Mark Miss
                                                            notification = "They Missed!";
                                                }
                                                if(battleship_health + carrier_health + cruiser_health + sub_health + patrol_health == 0)
                                                {
                                                      System.out.println("Message: " + message);
                                                      message = "GAME OVER" + message.substring(4);
                                                      tcp_out.println(message);
                                                      System.out.println("You Lost!"); 
                                                      try{Thread.sleep(5000);}catch(Exception e){System.exit(0);} // Graceful Exit
                                                      System.exit(0);
                                                }
                                                else{tcp_out.println(message);}
                                                
                                                System.out.println(opponent_board.GetImage() + "\n\n ------------------------------------\n\n" + client_board.GetImage());
                                                System.out.println(notification);
                                                    
                                          }// WHILE in Session
                                             
                                    }// IF
                              }// TRY
                              catch(Exception e)
                              {
                                    
                                    e.printStackTrace();
                                    System.exit(0);
                              }    
                        }// FOR
                          
                  }
                  catch(Exception e)
                  {
                        //System.out.println("UDP Fail");
                        e.printStackTrace();
                        System.exit(0);
                  }


                  
            }// WHILE not connected
            while(t1.isAlive())
            {
                  // wait while a session is ongoing in the thread
                  try{Thread.sleep(2500);}catch(Exception e){e.printStackTrace();System.exit(0);}
                  
            }
      
      }// MAIN
}
