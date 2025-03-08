import java.awt.*; 
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList; //stores our pipes
import java.util.Random; //placing pipes at random positions
import javax.swing.*;


public class MetalSonic extends JPanel implements ActionListener, KeyListener{
    int screenWidth = 360;
    int screenHeight = 640;

    //Vars storing images
    Image backgroundImg;
    Image playerImg;
    Image topTowerImg;
    Image bottomTowerImg;
    Image titleImg;
    Image creditImg;

     //Player:
    int playerX = screenWidth/8;
    int playerY = screenHeight/2;
    int playerWidth = 54; // change later
    int playerHeight = 44;
    Font PixelOperatorBold, PixelOperator;


    //Game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int endState = 3;
    public final int creditState = 4;
    public final int levelState = 5;

    public int commandNum = 0;

    //Music contr
    private Sound bgm;
    private Sound SE;


    class Player{
        int x = playerX;
        int y = playerY;
        int width = playerWidth;
        int height = playerHeight;
        Image img;

        Player(Image img){
            this.img = img;
        }
    }

    // Towers => obstacles
    int towerX = screenWidth;
    int towerY = 0;
    int towerWidth = 64; //scaled 1/6
    int towerHeight = 512;

    class Tower {
        int x = towerX;
        int y = towerY;
        int width = towerWidth;
        int height = towerHeight;
        Image img;
        boolean passed = false;

        Tower(Image img){
          this.img = img;  
        }

    }

    //game logic
    Player player;
    int velocityY = 0;
    int velocityX = -4; //to the left
    int gravity = 1;

    ArrayList<Tower> towers;
    Random random = new Random(); //random pipe placement s

    Timer gameLoop; //change name later
    Timer towerPlacementTimer;

   // boolean gameOver = false; ///continue game
    double score = 0;
    Sound sound = new Sound();
  //  BackgroundMusic bgMusic = new BackgroundMusic("/path/to/your/musicfile.wav");
    


    MetalSonic(){
        setPreferredSize(new Dimension(screenWidth,screenHeight));
        
        setFocusable(true);
        addKeyListener(this); //checks the three functions.Important
        //load images

        playerImg = new ImageIcon(getClass().getResource("./MetalSonc.png")).getImage();
        titleImg = new ImageIcon(getClass().getResource("./metalSoncChar.png")).getImage();
        creditImg = new ImageIcon(getClass().getResource("./creditImg.png")).getImage();

        player = new Player(playerImg);
        towers = new ArrayList<Tower>();
        bgm = new Sound();
        SE = new Sound();

        playMusic(3);
        

        

        //place pipes timer
        towerPlacementTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                towerPlacement();
            }
        });

       

        //owerPlacementTimer.start();

        //timer
        //gameLoop = new Timer(1000/60,this);
        //gameLoop.start();
        //gameState=playState; 
       // bgMusic.startMusic();
       

    }

    

    public void towerPlacement(){ //place new pipes
        int randomPipeY = (int) (towerY-towerHeight/4 - Math.random()*(towerHeight/2));
        int openingSpace = screenHeight/4;

        Tower topPipe = new Tower(topTowerImg);
        topPipe.y = randomPipeY;
        towers.add(topPipe);

        Tower bottomPipe = new Tower(bottomTowerImg);
        bottomPipe.y = topPipe.y + towerHeight + openingSpace;
        towers.add(bottomPipe);
    }

  

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){ //our background
        try {
            InputStream is = getClass().getResourceAsStream("/font/PixelOperatorBold.ttf");
            PixelOperatorBold = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/PixelOperator.ttf");
            PixelOperator = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


       if (gameState!=titleState&&gameState!=creditState&&gameState!=levelState){
        g.drawImage(backgroundImg, 0, 0, screenWidth, screenHeight, null); 
       
       //player
       g.drawImage(player.img, player.x,player.y,player.width,player.height,null);
   
        //pipes
        for (int i = 0; i<towers.size();i++){ //draw more pipes
            Tower pipe = towers.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y,pipe.width,pipe.height,null);
        }
        //score
       /* try {
            InputStream is = getClass().getResourceAsStream("/font/PixelOperatorBold.ttf");
            PixelOperatorBold = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/PixelOperator.ttf");
            PixelOperator = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/ 

        g.setFont(PixelOperatorBold);
        g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.PLAIN,32));

        
        
        if (gameState == endState){ // Shows score
            g.drawString("Try Again: " + String.valueOf((int)score),10,35);  
            
        } else if (gameState == pauseState){
            g.drawString("Current: " + String.valueOf((int)score),10,35);  
            g.setFont(g.getFont().deriveFont(Font.PLAIN,69));
            g.drawString("Paused",85,320);
            g.setFont(g.getFont().deriveFont(Font.PLAIN,18));
            g.drawString("!Press Enter to return to Select Screen!",21,400);
        } else{
            g.drawString(String.valueOf((int)score), 10,35);
         
        }
       } else if (gameState==creditState){
       
        //setBackground(Color.black);
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,screenWidth,screenHeight);
        g.setFont(PixelOperatorBold);
        //g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.PLAIN,50));
        //Shadows
        g.setColor(new Color(145,6,191));
        g.drawString("Credits",25,55);
        g.setColor(Color.white);
        g.drawString("Credits",20,50);
        g.setFont(g.getFont().deriveFont(Font.PLAIN,25));
        g.drawString("Assets: Sonic CD by SEGA® ",20,100);  
        g.drawString("Music: Naofumi Hataya and ",20,150); 
        g.drawString("Masafumi Ogata, Sonic CD ",20,170); 
        g.drawString("by SEGA® ",20,190); 
        g.drawString("Code: Alina Shayakhmetova,",20,240); 
        g.drawString("Computer Engineering Student",20,260); 
        g.drawString("at UOttawa (I love Sonic) ",20,280); 
        g.setFont(g.getFont().deriveFont(Font.PLAIN,20));
        g.setColor(new Color(145,6,191));
        g.drawString("!Press Esc to go back to Start Menu!",22,602); 
        g.setColor(Color.white);
        g.drawString("!Press Esc to go back to Start Menu!",20,600); 
        
        int x = screenWidth/2 -70;
        int y = screenHeight/2+10;
        g.drawImage(creditImg,x,y,445/3,620/3,null,null);


       } else if (gameState==levelState) { //Level Selection screen
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,screenWidth,screenHeight);
        g.setFont(PixelOperatorBold);
        //g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.PLAIN,50));
        g.setColor(Color.white);
        g.drawString("Level Select:",36,45);  

        //choices:
        g.setFont(g.getFont().deriveFont(Font.PLAIN,25));
        g.drawString("1 - Green Hills Zone",30,120);
        if (commandNum == 0){
            g.drawString(">",20,120);
        }
        g.drawString("2 - Marble Zone",30,150);
        if (commandNum == 1){
            g.drawString(">",20,150);
        }
        g.drawString("3 - Spring Yard Zone",30,180);
        if (commandNum == 2){
            g.drawString(">",20,180);
        }
        g.drawString("4 - Palmtree Panic",30,210);
        if (commandNum == 3){
            g.drawString(">",20,210);
        }
        g.drawString("5 - Quartz Quadrant",30,240);
        if (commandNum == 4){
            g.drawString(">",20,240);
        }
        g.drawString("6 - Wachy Workbench",30,270);
        if (commandNum == 5){
            g.drawString(">",20,270);
        }
        g.drawString("7 - Metallic Madness",30,300);
        if (commandNum == 6){
            g.drawString(">",20,300);
        }
        g.drawString("8 - Collision Chaos",30,330);
        if (commandNum == 7){
            g.drawString(">",20,330);
        }
        g.drawString("9 - Tidal Tempest",30,360);
        if (commandNum == 8){
            g.drawString(">",20,360);
        }
        g.drawString("10 - Stardust Speedway",30,390);
        if (commandNum == 9){
            g.drawString(">",20,390);
        }

        g.setFont(g.getFont().deriveFont(Font.PLAIN,20));
        g.setColor(new Color(145,6,191));
        g.drawString("!Press Space to select level!",48,455); 
        g.setColor(Color.white);
        g.drawString("!Press Space to select level!",45,450); 
        

       }else{
        //Title screen menu
        //setBackground(Color.black);
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,screenWidth,screenHeight);
        g.setFont(PixelOperatorBold);
        //g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.PLAIN,50));
        //Shadows
        g.setColor(new Color(145,6,191));
        g.drawString("Flappy Bird",55,50);


        g.setColor(Color.white);
        g.drawString("Flappy Bird",50,45);  
        g.setFont(g.getFont().deriveFont(Font.PLAIN,25));
        g.drawString("ft. Metal Sonic",90,85);  

        //Metal Sonic image
        int x = screenWidth/2 -30;
        int y = screenHeight/2-150;
        g.drawImage(titleImg,x,y,445/8,620/8,null,null);

        //menu
        g.setFont(g.getFont().deriveFont(Font.PLAIN,35));
        g.drawString("PLAY",130,350);
        if (commandNum == 0){
            g.drawString(">",115,350);

        }
        g.drawString("QUIT",140,410);
        if (commandNum == 1){
            g.drawString(">",125,410);

        }
        g.drawString("CREDITS",115,470);
        if (commandNum == 2){
            g.drawString(">",100,470);

        }

       }
    }


    

    public void playMusic(int i){
        bgm.setFile(i);
        bgm.play();
        bgm.loop();
    }

    public void stopMusic(){
        if (bgm!=null){
            bgm.stop();
        }
    }

    public void playSE(int i){
        SE.setFile(i);
        SE.play();
    }

    public void stopSE(){
        if (SE!=null){
            SE.stop();
        }
    }



    public void move(){

        //pipes
        for (int i = 0; i<towers.size();i++){
            Tower tower = towers.get(i);
            tower.x += velocityX; 
            
            if (!tower.passed&&player.x>tower.x+tower.width){
                tower.passed = true;
                score += 0.5;
            }

        

            if (collision(player,tower)){
                gameState = endState;

            }
        }


        //player
        velocityY += gravity;
        player.y += velocityY;
        player.y = Math.max(player.y,0); //stops at 0 if it reaches the top
        

        if (player.y >screenHeight){
            gameState = endState;
            //gameOver=true;
        }
    
    }

    public boolean collision(Player a, Tower b){
        return (a.x<b.x+b.width) && (a.x+a.width>b.x)&&(a.y<b.y+b.height)&&(a.y+a.height>b.y);
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
       move();
       repaint(); //calls paint component
       if (gameState == endState){
        towerPlacementTimer.stop();
        gameLoop.stop();
        playSE(2); 
       }
    }


    @Override
    public void keyPressed(KeyEvent e) {//any key

        if (e.getKeyCode() == KeyEvent.VK_ENTER && gameState==titleState){
            if (commandNum==0){
                gameState = levelState;
                repaint(); //calls paint component
            } else if (commandNum==1){  //Quit 
                System.exit(0);
            } else if (commandNum==2){
                gameState = creditState;
                repaint();
            } 
        } 

        if (e.getKeyCode() == KeyEvent.VK_ENTER && gameState==pauseState){
            stopMusic();
            gameState = levelState;
            playMusic(3);
            repaint();
        } 
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameState==levelState){
            int choiceSong = (commandNum+5);
            stopMusic();
            stopSE();
            String bottomPipeName = "/bottomPipes/"+("b"+ String.valueOf(commandNum+1))+".png";
            String topPipeName = "/topPipes/"+("t"+ String.valueOf(commandNum+1))+".png";
            String bgName ="/bgs/"+("bg"+ String.valueOf(commandNum+1))+".png";
            backgroundImg = new ImageIcon(getClass().getResource(bgName)).getImage();  
            topTowerImg = new ImageIcon(getClass().getResource(topPipeName)).getImage();
            bottomTowerImg = new ImageIcon(getClass().getResource(bottomPipeName)).getImage();   
            repaint(); //calls paint component
            gameState=playState;
            towerPlacementTimer.start();
            gameLoop = new Timer(1000/60,this);
            gameLoop.start(); 
            playMusic(choiceSong);
        } 

        if ((e.getKeyCode() == KeyEvent.VK_W && gameState == titleState)) {
            commandNum--;
            repaint();
            playSE(4);
            if (commandNum<0){
                commandNum=2; 
                //System.out.println("meow" +gameState+commandNum);          
            }
        } 
    
        if ((e.getKeyCode() == KeyEvent.VK_S && gameState == titleState)) {
            commandNum++;
            repaint();
            playSE(4);
            if (commandNum>2){
                commandNum=0;
                //System.out.println("bark" +gameState+commandNum);    
            }
        } 

        if (gameState == levelState) { //level selection
            if (e.getKeyCode() == KeyEvent.VK_W ){
                commandNum = (commandNum - 1 + 10) % 10; // Wrap from 0 to 9
                repaint();
                playSE(4);
            }
                
            if (e.getKeyCode() == KeyEvent.VK_S){
                commandNum = (commandNum + 1) % 10; // Wrap from 0 to 9
                repaint();
                playSE(4);
            }
        } 

       

        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameState!=titleState && gameState!=levelState ){
            velocityY = -9; //going up
            playSE(1);

            if (gameState == endState){
                //restart game - reset conditions
                player.y = playerY;
                velocityY = 0;
                towers.clear();
                score = 0;
                gameState = playState;
                gameLoop.start();
                towerPlacementTimer.start();
            } else if (gameState == pauseState){
                stopSE(); 
            }
        } 

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            if(gameState==pauseState){
                gameState=playState;
                towerPlacementTimer.start();
                gameLoop.start();

            } else if (gameState==endState){
                gameState=endState;
            }else if(gameState==creditState){
                gameState=titleState;
                repaint();
            } else if (gameState == levelState) {
                gameState=titleState;
                repaint();
            }else{
                gameState=pauseState;
                towerPlacementTimer.stop();
                gameLoop.stop();  
                repaint();  
            }
        }



    }
    
    @Override
    public void keyTyped(KeyEvent e) {}//only letters



    @Override
    public void keyReleased(KeyEvent e) {}



}