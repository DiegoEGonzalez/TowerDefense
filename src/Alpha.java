import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Alpha extends JPanel implements MouseListener,MouseWheelListener, MouseMotionListener{
    static int mapsize=1500;
    static int maxMapsize=5000;

    ArrayList<Unit> objects = new ArrayList<Unit>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    static ArrayList<Particles> fx = new ArrayList<Particles>();

    double scaler =1.0/((double)mapsize/800.0);
    int startx=0;
    int offsetx=(int)(-400/scaler);
    int starty=0;
    int offsety=0;//(int)(-400/scaler);
    int currentx=0;
    int currenty=0;
    static int selection =0;
    long lastAction=gametime;
    int level=1;
    boolean start=false;

    boolean running=false;
    long startPause=0; //value to store when pause was pressed
    long startGame=0; //value to store when game started
    static long gametime=0; //time game has been runnning

    boolean beacon=false;


    static Unit selected;
    boolean spawnSelected=false;

    //variables for stars
    ArrayList <Integer> starX = new ArrayList<Integer>(); // x star positions
    ArrayList <Integer> starY = new ArrayList<Integer>(); // y star positions
    //whether or not to display developer shit
    static boolean developer=false;
    int safebase = 600;
    int numberOfAsteroids=50;

    User user;
    Enemy enem;


    int userx=0;
    int usery=0;


    public Alpha() {
        setLayout(null);
        setSize(Shell.DEFAULT_WINDOWSIZEX, Shell.DEFAULT_WINDOWSIZEY);
        setBackground(Color.BLACK);
        setVisible(true);
        setDoubleBuffered(true);
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        key();

        objects.add(new BASE(50,0, 1, objects, lasers));
        selected=objects.get(0);
        selection=5;
        user = new User();
        enem= new Enemy(objects,lasers);

        for(int x=0;x<numberOfAsteroids;x++){
            int sizerandom=0;
            int spawnx=0;
            int spawny=0;
            do{
                sizerandom=(int)(Math.random()*100)+20;
                spawnx=(int)(Math.random()*(mapsize+mapsize/4));
                spawny=(int)(Math.random()*(mapsize+mapsize/2))-mapsize/4-mapsize/2;
            } while(collisionCircle(spawnx,spawny,sizerandom/2,50,0,User.basefield/2));
            objects.add(new Asteroid(spawnx,spawny,sizerandom,sizerandom,objects,lasers));
        }

        for(int x=0;x<1000;x++){ //1000 stars
            //randomly spawns stars
            starX.add((int)(Math.random()*mapsize*4)-mapsize);
            starY.add((int)(Math.random()*mapsize*4)-mapsize*2);
        }

        startGame=System.nanoTime();
        startPause=System.nanoTime();

    }

    public void key(){
        // ---------- SPACE ---------------
        Action space = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if(running){
                    startPause=System.nanoTime();
                } else {
                    startGame+=System.nanoTime()-startPause;
                }
                running=!running;



            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("SPACE"),
                "space");
        getActionMap().put("space",
                space);


        Action num1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                selection=1;

            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("1"),
                "num1");
        getActionMap().put("num1",
                num1);
        Action num2 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                selection=2;

            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("2"),
                "num2");
        getActionMap().put("num2",
                num2);
        Action num3 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                selection=3;

            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("3"),
                "num3");
        getActionMap().put("num3",
                num3);
        Action num4 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                selection=4;

            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("4"),
                "num4");
        getActionMap().put("num4",
                num4);
        Action num0 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                selection=0;

            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("0"),
                "num0");
        getActionMap().put("num0",
                num0);

        Action d = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                developer=!developer;

            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("D"),
                "D");
        getActionMap().put("D",
                d);
    }

    public void update(){


        if(running){
            gametime=System.nanoTime()-startGame;

        if((gametime-lastAction)/1000000000.0>5&&start){
            start=false;
            level++;
            enem.increase();
        }

        if(enem.spawning){
            enem.spawn();
        }

        for(int x=0;x<objects.size();x++){
            objects.get(x).update();
        }
        for(int x=0;x<lasers.size();x++){
            lasers.get(x).update();
        }
        for(int x=0;x<fx.size();x++){
            fx.get(x).update();
        }

        int ttt=0;
        int sss=0;
        int www=0;
        int eee=0;
        int enemy=0;

        for (Iterator<Unit> iterator = objects.iterator(); iterator.hasNext(); ) {

            Unit b = iterator.next();

            if(!b.alive) {
                        switch (b.kind){
                            case 1:
                                for (int y = 0; y < 10; y++)
                                fx.add(new Particles(Color.BLUE, (int) (Math.random() * 5) + 1, b.x, b.y, (int) (Math.random()*3), Math.random() * Math.toRadians(360), ((b.power/2 * Math.random())+b.power/2)*2,0));
                                break;
                            case 2:
                                for (int y = 0; y < 10; y++)
                                fx.add(new Particles(Color.RED, (int) (Math.random() * 5) + 1, b.x, b.y, (int) (Math.random()*3), Math.random() * Math.toRadians(360), ((b.power/2 * Math.random())+b.power/2)*2,0));
                                break;
                            case 0:
                                if(((Asteroid)b).isMined()) {
                                    for (int y = 0; y < 10; y++)
                                    fx.add(new Particles(new Color(51, 25, 0), (int) (Math.random() * b.h / 4) + 1, b.x, b.y, (int) (Math.random() * 3), Math.random() * Math.toRadians(360), (2 * Math.random() + 1), 1));
                                    for (int y = 0; y < 10; y++)
                                    fx.add(new Particles(new Color(0,255,0,(100)),(int) (Math.random() * b.h / 8) + 1,b.x,b.y,(Math.random() * 1), Math.random()*Math.toRadians(360),(2 * Math.random() + 1),1));
                                    user.moolah+=b.h;
                                } else {
                                    for (int y = 0; y < 10; y++)
                                        fx.add(new Particles(new Color(51, 25, 0), (int) (Math.random() * b.h / 4) + 1, b.x, b.y, (int) (Math.random() * 3), Math.random() * Math.toRadians(360), (2 * Math.random() + 1), 1));
                                    user.moolah+=b.h/5;
                                }
                                break;
                        }


                if(b instanceof TTT){
                    if(b.kind!=1)
                    user.moolah+=2;
                } else if(b instanceof SSS){

                    user.moolah+=10;
                } else if(b instanceof WWW){

                    user.moolah+=30;
                }

                if(b == selected&&selection==5)
                    selection=0;


                iterator.remove();

            }

            if( b instanceof TTTSpawner)
                ttt++;
            if( b instanceof SSSSpawner)
                sss++;
            if( b instanceof WWWSpawner)
                www++;
            if( b instanceof EEESpawner)
                eee++;



        }
            user.TTT=ttt;
            user.SSS=sss;
            user.WWW=www;
            user.EEE=eee;


        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
            Laser b = iterator.next();
            if(!b.isAlive()) {
                iterator.remove();

            }
        }
        for (Iterator<Particles> iterator = fx.iterator(); iterator.hasNext(); ) {
            Particles b = iterator.next();
            if(!b.isAlive()) {
                iterator.remove();
            }

        }

        if(enemy==0&&!start){
            start=true;
            lastAction=gametime;
        }

        } else {
            gametime=startPause-startGame;
        }

    }

    public void addMeteor(){
        if(Math.random()<.05) {
            for (int x = 0; x < 1; x++) {
                int sizerandom = 0;
                int spawnx = 0;
                int spawny = 0;
                boolean inSpawnzone = false;
                do {
                    sizerandom = (int) (Math.random() * 100) + 20;
                    spawnx = (int) (Math.random() * (mapsize + mapsize / 4));
                    spawny = (int) (Math.random() * (mapsize + mapsize / 2)) - mapsize / 4 - mapsize / 2;
                    inSpawnzone = collision(spawnx - sizerandom / 2, spawny - sizerandom / 2, sizerandom, sizerandom, 0, -mapsize / 2, mapsize, mapsize);

                } while (inSpawnzone);

                objects.add(new Asteroid(spawnx, spawny, sizerandom, sizerandom, objects, lasers));
                ;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D a = (Graphics2D)g;

        a.translate(200,0); //shifts game 200 pixels to make room for UI
        a.translate(400,400); //shifts anchor point to the center in orderfor accurate transformation 
        a.scale(scaler,scaler); //applies zoom(scaler)



        a.scale(.5,.5);
        a.translate((offsetx),(offsety));
        a.setColor(Color.DARK_GRAY);
        for(int x=0;x<starX.size();x++){
            a.drawOval(starX.get(x),starY.get(x),1,1);
        }
        a.translate(-(offsetx),-(offsety));
        a.scale(2,2);



        a.translate((offsetx),(offsety)); // applies offset

        if(developer) {
            g.setColor(Color.GRAY);
            g.drawRect(0, -mapsize / 2, mapsize, mapsize); //draws map
            g.drawRect(0, -mapsize / 2 - mapsize / 4, mapsize + mapsize / 4, mapsize + mapsize / 2); // draws meteor spawn area
            g.drawRect(0, -mapsize / 2 - mapsize / 4 - mapsize / 8, mapsize + mapsize / 4 + mapsize / 8, mapsize + mapsize / 2 + mapsize / 4); // draws spawn area
            g.setColor(Color.WHITE);
        }


        for(int x=0;x<objects.size();x++){
            objects.get(x).draw(a);
        }
        for(int x=0;x<lasers.size();x++){
            lasers.get(x).draw(g);
        }
        for(int x=0;x<fx.size();x++){
            fx.get(x).draw(g);
        }

        ///beacon
        if(User.beaconing&&running) {
            a.setColor(new Color(255, 255, 0, 15));
            a.fillOval(User.userx-150, User.userY-150, 300, 300);

        }

        //undoes the transformation
        a.translate(-(offsetx),-(offsety));
        a.scale(1.0/scaler,1.0/scaler);
        a.translate(-400,-400);
        a.translate(-200,0);

        drawUI(g);
        if (developer)
            drawDev(g);


    }

    public void drawUI(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,200,Shell.DEFAULT_WINDOWSIZEY);
        g.setColor(Color.WHITE);
        g.drawRect(0,0,200,Shell.DEFAULT_WINDOWSIZEY);
        g.drawRect(0,0,200,100);
        g.drawString("\\\\ RETRO RIVALS //",center(200,"\\\\ RETRO RIVALS //",g),30);
        g.drawString("<< ALPHA TEST v.09 >>",center(200,"<< ALPHA TEST v.09 >>",g),60);

        g.drawString("^^^ : "+user.moolah,10,130);
        g.drawString("wave : " +(level-1),10,150);

        g.setColor(Color.WHITE);
        g.drawRect(0, 175, 200, 50);

        if(selection==1)
            g.drawRect(5, 180, 190, 40);
        if(user.moolah<((int)((double)(user.TTT+1)/user.maxTTT*user.maxTTTCost)))
            g.setColor(Color.DARK_GRAY);

        g.drawString(" ^^^ : "+((int)((double)(user.TTT+1)/user.maxTTT*user.maxTTTCost))+" | TTT",10,200);

        g.setColor(Color.WHITE);
        g.drawRect(0,225,200,50);
        if(selection==2)
            g.drawRect(5,230,190,40);
        if((user.moolah<(int)((double)(user.SSS+1)/user.maxSSS*user.maxSSSCost)&&user.unlockedSSS)||(user.moolah<100&&!user.unlockedSSS))
            g.setColor(Color.DARK_GRAY);

        if(user.unlockedSSS){
            g.drawString(" ^^^ : "+(int)((double)(user.SSS+1)/user.maxSSS*user.maxSSSCost)+" | SSS",10,250);
        } else{
            g.drawString(" ^^^ : 100 | UNLOCK SSS ",10,250);
        }

        g.setColor(Color.WHITE);
        g.drawRect(0,275,200,50);
        if(selection==3)
            g.drawRect(5,280,190,40);
        if((user.moolah<(int)((double)(user.WWW+1)/user.maxWWW*user.maxWWWCost)&&user.unlockedWWW)||(user.moolah<200&&!user.unlockedWWW))
            g.setColor(Color.DARK_GRAY);

        if(user.unlockedWWW){
            g.drawString(" ^^^ : "+(int)((double)(user.WWW+1)/user.maxWWW*user.maxWWWCost)+" | WWW",10,300);
        } else{
            g.drawString(" ^^^ : 200 | UNLOCK WWW ",10,300);
        }


        g.setColor(Color.WHITE);
        g.drawRect(0,325,200,50);
        if(selection==4)
            g.drawRect(5,330,190,40);
        if((user.moolah<(int)((double)(user.EEE+1)/user.maxEEE*user.maxEEECost)&&user.unlockedEEE)||(user.moolah<200&&!user.unlockedEEE))
            g.setColor(Color.DARK_GRAY);

        if(user.unlockedEEE){
            g.drawString(" ^^^ : "+(int)((double)(user.EEE+1)/user.maxEEE*user.maxEEECost)+" | EEE",10,350);
        } else{
            g.drawString(" ^^^ : 500 | UNLOCK EEE ",10,350);
        }


        ///////////////////// USER STRATEGY BOX ///////////////

        g.setColor(Color.WHITE);
        g.drawRect(0,500,200,300);
        switch (selection){
            case 1:
                g.drawString("TTT",80,530);
                g.drawString("> basic unit.",20,560);
                g.drawString("[ speed ] : "+User.TTTspeed,20,590);
                g.drawString("[ damage ] : "+User.TTTdamage,20,610);
                g.drawString("[ sensor ] : "+User.TTTsearch,20,630);
                g.drawString("[ range ] : "+User.TTTrange,20,650);
                break;
            case 2:
                g.drawString("SSS",80,530);
                g.drawString("> fast aggressive unit.",20,560);
                g.drawString("[ speed ] : "+User.SSSspeed,20,590);
                g.drawString("[ damage ] : "+User.SSSdamage,20,610);
                g.drawString("[ sensor ] : "+User.SSSsearch,20,630);
                g.drawString("[ range ] : "+User.SSSrange,20,650);
                break;
            case 3:
                g.drawString("WWW",80,530);
                g.drawString("> strong heavy tank unit.",20,560);
                g.drawString("[ speed ] : "+User.WWWspeed,20,590);
                g.drawString("[ damage ] : "+User.WWWdamage,20,610);
                g.drawString("[ sensor ] : "+User.WWWsearch,20,630);
                g.drawString("[ range ] : "+User.WWWrange,20,650);
                break;
            case 4:
                g.drawString("EEE",80,530);
                g.drawString("> mining unit.",20,560);
                g.drawString("[ speed ] : "+User.EEEspeed,20,590);
                g.drawString("[ damage ] : "+User.EEEdamage,20,610);
                g.drawString("[ sensor ] : "+User.EEEsearch,20,630);
                g.drawString("[ range ] : "+User.EEErange,20,650);
                break;
            case 5:
                if(selected instanceof Spawner) {
                    g.drawString(((Spawner)selected).name, center(200, ((Spawner)selected).name, g), 530);
                    g.drawString(((Spawner)selected).count + "/" + ((Spawner)selected).maxcount, 20, 560);
                    long timealive = (Alpha.gametime - ((Spawner)selected).birthdate) / 1000000000;
                    g.drawString("[ lifetime ] : " + timealive + "s", 20, 580);
                    g.drawString("[ rate ] : " + selected.recharge, 20, 600);
                    g.drawString("[ health ] : " + selected.health + "/" + selected.maxhealth, 20, 620);
                    g.drawString("[ shields ] : " + ((((Spawner)selected).forceon)?"ON":"OFF"), 20, 640);

                    if(selected instanceof TTTSpawner)
                        g.drawString("SELL ^^^ : " + (int)(((double) (user.TTT + 1) / user.maxTTT * user.maxTTTCost) * ((double) (user.TTT + 1) / user.maxTTT)), center(200, "SELL ^^^ : " + (int)(((double) (user.TTT + 1) / user.maxTTT * user.maxTTTCost) * ((double) (user.TTT + 1) / user.maxTTT)), g), 700);
                    if(selected instanceof SSSSpawner)
                        g.drawString("SELL ^^^ : " + (int)(((double) (user.SSS + 1) / user.maxSSS * user.maxSSSCost) * ((double) (user.SSS + 1) / user.maxSSS)), center(200, "SELL ^^^ : " + (int)(((double) (user.SSS + 1) / user.maxSSS * user.maxSSSCost) * ((double) (user.SSS + 1) / user.maxSSS)), g), 700);
                    if(selected instanceof WWWSpawner)
                        g.drawString("SELL ^^^ : " + (int)(((double) (user.WWW + 1) / user.maxWWW * user.maxWWWCost) * ((double) (user.WWW + 1) / user.maxWWW)), center(200, "SELL ^^^ : " + (int)(((double) (user.WWW + 1) / user.maxWWW * user.maxWWWCost) * ((double) (user.WWW + 1) / user.maxWWW)), g), 700);
                    if(selected instanceof EEESpawner)
                        g.drawString("SELL ^^^ : " + (int)(((double) (user.EEE + 1) / user.maxEEE * user.maxEEECost) * ((double) (user.EEE + 1) / user.maxEEE)), center(200, "SELL ^^^ : " + (int)(((double) (user.EEE + 1) / user.maxEEE * user.maxEEECost) * ((double) (user.EEE + 1) / user.maxEEE)), g), 700);
                    g.drawRect(25,675,150,40);

                } else {
                    g.drawString("BASE",center(200,"BASE",g),530);
                    g.drawString("> protect.",20,560);
                    long timealive = (Alpha.gametime - ((BASE)selected).birthdate) / 1000000000;;
                    g.drawString("[ lifetime ] : " + timealive + "s", 20, 590);
                    g.drawString("[ forcefield ] : " + User.basefield, 20, 610);
                    g.drawString("DIRECT",center(200,"DIRECT",g),660);
                    g.drawRect(50, 635, 100, 40);
                    if(beacon)
                    g.drawRect(55,640,90,30);

                }
                break;
        }

        //////////// PAUSE SCREEN //////////////////////////
        if(!running)
        g.drawString("Paused.",200+center(800,"Paused.",g),400);

    }

    public void drawDev(Graphics g){
        g.drawString("DEVELOPER VERSION",220,40);
        g.drawString("units: "+objects.size(),220,60);
        g.drawString("lasers: "+lasers.size(),220,80);
        g.drawString("particles: "+fx.size(),220,100);
        g.drawString("mapsize: "+mapsize+"m",220,120);
        g.drawString("zoommap: "+(800/scaler)+"m",220,140);
        g.drawString("zoom: "+scaler+"x",220,160);
        g.drawString("x:"+userx+" y:"+usery,220,180);
        g.drawString("Time: "+(gametime/1000000)+"s",220,200);

    }

    public int center(int x, String a, Graphics g){
        Graphics2D text = (Graphics2D)g;
        FontMetrics fm=g.getFontMetrics();
        Rectangle2D rect=fm.getStringBounds(a,text);
        return (int)((x- rect.getWidth())/2);
    }

    public boolean collision(int x, int y, int a, int b, int w, int h){
        boolean up=(y-b)<h;
        boolean down=(y-b)>0;
        boolean left=(x-a)<w;
        boolean right=(x-a)>0;

        if(left&&right&&up&&down){
            return true;
        }
        return false;
    }

    public boolean collisionCircle(int x1, int y1, int r1, int x2, int y2, int r2){
        double xDif = x1 - x2;
        double yDif = y1 - y2;
        double distanceSquared = xDif * xDif + yDif * yDif;

        return (distanceSquared < (r1 + r2) * (r1 + r2));
    }

    public boolean collision(int x, int y, int w, int h, int x1, int y1, int w1, int h1){
        //checks the 4 corners in order to check for collision, possible error, checks the first variable's corners
        return collision(x,y,x1,y1,w1,h1)||collision(x+w,y,x1,y1,w1,h1)||collision(x,y+h,x1,y1,w1,h1)||collision(x+w,y+h,x1,y1,w1,h1);
    }

    public void mouseClicked(MouseEvent e) {

        //the x and y position of the cursor within the world, applies the transformations to the x and y
        int worldX=(int)(((e.getX()-200-400)/scaler)-offsetx); // tricky but goes backwards from transformations.
        int worldY=(int)(((e.getY()-400)/scaler)-offsety);

        if(e.getX()<200) {
            if (collision(e.getX(), e.getY(), 0, 175, 200, 50)&&selection==1)
                selection = 0;
            else if (collision(e.getX(), e.getY(), 0, 225, 200, 50)&&selection==2)
                selection = 0;
            else if (collision(e.getX(), e.getY(), 0, 275, 200, 50)&&selection==3)
                selection = 0;

            if (collision(e.getX(), e.getY(), 0, 175, 200, 50)){
                selection = 1;
            }else if (collision(e.getX(), e.getY(), 0, 225, 200, 50)&&((!user.unlockedSSS&&user.moolah>100)||user.unlockedSSS)){
                if(!user.unlockedSSS){
                    user.unlockedSSS=true;
                    user.moolah-=100;
                }else
                    selection = 2;
            }else if (collision(e.getX(), e.getY(), 0, 275, 200, 50)&&((!user.unlockedWWW&&user.moolah>200)||user.unlockedWWW)){
                if(!user.unlockedWWW){
                    user.unlockedWWW=true;
                    user.moolah-=200;
                }else
                    selection = 3;
            }else if (collision(e.getX(), e.getY(), 0, 325, 200, 50)&&((!user.unlockedEEE&&user.moolah>500)||user.unlockedEEE)){
                if(!user.unlockedEEE){
                    user.unlockedEEE=true;
                    user.moolah-=500;
                }else
                    selection = 4;
            }

            if(selection==5&&selected instanceof BASE&&collision(e.getX(),e.getY(),50,635,100,40))
                beacon=!beacon;
            if(selection==5&&selected instanceof Spawner&&collision(e.getX(),e.getY(),25,675,150,40)){
                if(selected instanceof TTTSpawner)
                user.moolah+=(int)(((double) (user.TTT + 1) / user.maxTTT * user.maxTTTCost) * ((double) (user.TTT + 1) / user.maxTTT));
                if(selected instanceof SSSSpawner)
                    user.moolah+=(int)(((double) (user.SSS + 1) / user.maxSSS * user.maxSSSCost) * ((double) (user.SSS + 1) / user.maxSSS));
                if(selected instanceof WWWSpawner)
                    user.moolah+=(int)(((double) (user.WWW + 1) / user.maxWWW * user.maxWWWCost) * ((double) (user.WWW + 1) / user.maxWWW));
                if(selected instanceof EEESpawner)
                    user.moolah+=(int)(((double) (user.EEE + 1) / user.maxEEE * user.maxEEECost) * ((double) (user.EEE + 1) / user.maxEEE));

                selection=0;
                int i=objects.indexOf(selected);
                objects.remove(i);
            }






        } else if (e.getX()>225) {

            //////////////////////////////////////////////////////////////
            //                                                          //
            //                                                          //
            //              ACTIONS WITHING THE MAP                     //
            //                                                          //
            //                                                          //
            //////////////////////////////////////////////////////////////

//hitler is our friend
            //variable that determines whether or not there is another block already beneath the cursor
            boolean cheaterprevention=false;
            int spawnclicked=-1; // variable to store which spawner the user is clicking on

            //checks the position of all the objects in the array and makes sure it's not under the cursor
            for(int x=0;x<objects.size();x++){
                if(objects.get(x)instanceof Spawner|| objects.get(x) instanceof BASE||objects.get(x).kind==3){ //only considers objects that are spawners or asteroids
                    //bad way to check all 4 corners
                    if(collision(worldX-25,worldY-25,objects.get(x).getX(),objects.get(x).getY(),objects.get(x).w,objects.get(x).h)||collision(worldX+25,worldY-25,objects.get(x).getX(),objects.get(x).getY(),objects.get(x).w,objects.get(x).h)||collision(worldX-25,worldY+25,objects.get(x).getX(),objects.get(x).getY(),objects.get(x).w,objects.get(x).h)||collision(worldX+25,worldY+25,objects.get(x).getX(),objects.get(x).getY(),objects.get(x).w,objects.get(x).h)){
                        cheaterprevention=true;//says there is an object beneath the cursor
                        if(objects.get(x) instanceof Spawner||objects.get(x) instanceof BASE)
                        spawnclicked=x;
                    }
                }
            }

            if(!cheaterprevention){

                switch (selection){

                    case 1:
                        if(running&&user.moolah>=((double)(user.TTT+1)/user.maxTTT*user.maxTTTCost)&&user.TTT<user.maxTTT||developer) {
                            objects.add(new TTTSpawner(worldX, worldY, 1, objects, lasers));
                            user.moolah -= (int)((double)(user.TTT+1)/user.maxTTT*user.maxTTTCost);
                        }
                        selection = 0;
                        break;
                    case 2:
                        if(running&&user.moolah>=(int)((double)(user.SSS+1)/user.maxSSS*user.maxSSSCost)||developer) {
                            objects.add(new SSSSpawner(worldX, worldY, 1, objects, lasers));
                            user.moolah -= (int)((double)(user.SSS+1)/user.maxSSS*user.maxSSSCost);
                        }
                        selection = 0;
                        break;
                    case 3:
                        if(running&&user.moolah>=(int)((double)(user.WWW+1)/user.maxWWW*user.maxWWWCost)||developer) {
                            objects.add(new WWWSpawner(worldX, worldY, 1, objects, lasers));
                            user.moolah -= (int)((double)(user.WWW+1)/user.maxWWW*user.maxWWWCost);

                        }
                        selection=0;
                        break;
                    case 4:
                        if(running&&user.moolah>=(int)((double)(user.EEE+1)/user.maxEEE*user.maxEEECost)||developer){
                        objects.add(new EEESpawner(worldX, worldY, 1, objects, lasers));
                        user.moolah -=(int)((double)(user.EEE+1)/user.maxEEE*user.maxEEECost);
                        }
                        selection=0;
                        break;
                    case 5:
                        selection=0;
                        break;

                }
            } else {
                if((selection==0||selection==5)&&spawnclicked!=-1){
                    selection=5;
                    selected=objects.get(spawnclicked);
                }
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        //if(selection==0) {
            startx = e.getX();
            starty = e.getY();
        //}

    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if(e.getX()>200&&beacon){
            User.beaconing=true;
            //the x and y position of the cursor within the world, applies the transformations to the x and y
            int worldX=(int)(((e.getX()-200-400)/scaler)-offsetx); // tricky but goes backwards from transformations.
            int worldY=(int)(((e.getY()-400)/scaler)-offsety);
            User.userx=worldX;
            User.userY=worldY;
            userx=worldX;
            usery=worldY;
        }
    }


    public void mouseExited(MouseEvent e) {
        User.beaconing=false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //whether or not it allows the user to zoom out past the map
        boolean allowDebug = developer;

        int amount = e.getWheelRotation(); //amount wheel was rotated
        double defaultscaler = 1.0/((double)mapsize/(800));
        double zoom = -(amount/100.0); // amount that will be zoomed
        boolean zoomOut = scaler+zoom>((allowDebug)?0:defaultscaler); // makes sure zoom will not be negative and flip the screen
        boolean zoomIn = scaler+zoom<2; //makes sure that the zoom won't be too much 2x zoom

        if(zoomOut&&zoomIn)
            scaler -= amount / 100.0; // applies zoom

        int sceneW = 800; //width of scene in which the camera is in
        int sceneH = 800; //height of scene in which the camera is in
        double xMax = (sceneW/2)/scaler - (mapsize);
        double xMin = -(sceneW/2)/scaler;
        double yMax = (sceneH/2)/scaler - mapsize/2;
        double yMin = -(sceneH/2)/scaler+mapsize/2;

        if(scaler<defaultscaler){
            //moves the offset when you are zoomed out of map

            if(!(offsetx>xMin&&offsetx<xMax)){
                //changes the offset to be within scene
                if(offsetx<xMin)
                    offsetx=(int)xMin;
                if(offsetx>xMax)
                    offsetx=(int)xMax;
            }

            if(!(offsety>yMin&&offsety<yMax)){
                //changes the offset to be within scene
                if(offsety<yMin)
                    offsety=(int)yMin;
                if(offsety>yMax)
                    offsety=(int)yMax;
            }
        } else {
            //moves the offset when you are zoomed in the map

            if((offsetx<xMin||offsetx>xMax)&&(!developer)){
                //changes the offset to be within map
                if(offsetx>xMin)
                    offsetx=(int)xMin;
                if(offsetx<xMax)
                    offsetx=(int)xMax;
            }

            if((offsety<yMin||offsety>yMax)&&(!developer)){
                //changes the offset to be within map
                if(offsety>yMin)
                    offsety=(int)yMin;
                if(offsety<yMax)
                    offsety=(int)yMax;
            }

        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //// **** IMPORTANT NOTE ***** remember that shifting it to the left is negative and right is positive
        //if(selection==0) {
            int sceneW = 800; //width of scene in which the camera is in
            int sceneH = 800; //height of scene in which the camera is in
            double defaultscaler = 1.0/((double)mapsize/(sceneW));

           if(e.getX()>=200) {
               if (scaler < defaultscaler) {
                   //make sure the map is within the scene
                   double xMax = (sceneW / 2) / scaler - (mapsize);
                   double xMin = -(sceneW / 2) / scaler;
                   double yMax = (sceneH / 2) / scaler - mapsize / 2;
                   double yMin = -(sceneH / 2) / scaler + mapsize / 2;
                   //calculates the offset taking the zoom in consideration
                   double tempoffsetX = offsetx + (e.getX() - startx) / scaler;
                   double tempoffsetY = offsety + (e.getY() - starty) / scaler;
                   if (tempoffsetX > xMin && tempoffsetX < xMax) //make sure the offset won't go out of scene
                       offsetx = (int) tempoffsetX;
                   else {
                       //changes the offset to be within scene
                       if (tempoffsetX < xMin)
                           offsetx = (int) xMin;
                       if (tempoffsetX > xMax)
                           offsetx = (int) xMax;
                   }

                   if (tempoffsetY > yMin && tempoffsetY < yMax)//make sure the offset won't go out of scene
                       offsety = (int) tempoffsetY;
                   else {
                       //changes the offset to be within scene
                       if (tempoffsetY < yMin)
                           offsety = (int) yMin;
                       if (tempoffsetY > yMax)
                           offsety = (int) yMax;
                   }

               } else {
                   //make sure the map is within the scene
                   double xMax = (sceneW / 2) / scaler - (mapsize);
                   double xMin = -(sceneW / 2) / scaler;
                   double yMax = (sceneH / 2) / scaler - mapsize / 2;
                   double yMin = -(sceneH / 2) / scaler + mapsize / 2;
                   //calculates the offset taking the zoom in consideration
                   double tempoffsetX = offsetx + (e.getX() - startx) / scaler;
                   double tempoffsetY = offsety + (e.getY() - starty) / scaler;

                   if ((tempoffsetX < xMin && tempoffsetX > xMax) || developer) //make sure the offset won't go out of scene
                       offsetx = (int) tempoffsetX;
                   if ((tempoffsetY < yMin && tempoffsetY > yMax) || developer)//make sure the offset won't go out of scene
                       offsety = (int) tempoffsetY;

               }
           }
            //resets the offset anchor
            startx = e.getX();
            starty = e.getY();
        //}

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(e.getX()>200&&beacon){
            User.beaconing=true;
            //the x and y position of the cursor within the world, applies the transformations to the x and y
            int worldX=(int)(((e.getX()-200-400)/scaler)-offsetx); // tricky but goes backwards from transformations.
            int worldY=(int)(((e.getY()-400)/scaler)-offsety);
            User.userx=worldX;
            User.userY=worldY;
            userx=worldX;
            usery=worldY;
        } else {
            User.beaconing=false;
        }


    }

    public long getTime(){
        return gametime;
    }
}
