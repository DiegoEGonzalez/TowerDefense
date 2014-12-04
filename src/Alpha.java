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
    ArrayList<Unit> objects = new ArrayList<Unit>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    static ArrayList<Particles> fx = new ArrayList<Particles>();
    double marginX = 200;
    double scaler =1.0/((double)mapsize/800.0);
    int startx=0;
    int offsetx=(int)(-400/scaler);
    int starty=0;
    int offsety=0;//(int)(-400/scaler);
    int currentx=0;
    int currenty=0;
    int selection =0;
    long lastAction=System.nanoTime();
    int level=1;
    boolean start=false;
    boolean running=false;

    //variables for stars
    ArrayList <Integer> starX = new ArrayList<Integer>(); // x star positions
    ArrayList <Integer> starY = new ArrayList<Integer>(); // y star positions
    //whether or not to display developer shit
    boolean developer=true;
    int safebase = 600;
    int numberOfAsteroids=50;

    User user;
    Enemy enem;

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
        //objects.add(new BASE(mapsize-50,mapsize/2-50,2,objects,lasers));
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
            } while(collision(spawnx-sizerandom/2,spawny-sizerandom/2,sizerandom,sizerandom,0,-safebase/2,safebase,safebase));
            objects.add(new Asteroid(spawnx,spawny,sizerandom,sizerandom,objects,lasers));
        }

        for(int x=0;x<1000;x++){ //1000 stars
            //randomly spawns stars
            starX.add((int)(Math.random()*mapsize*4)-mapsize);
            starY.add((int)(Math.random()*mapsize*4)-mapsize*2);
        }

    }

    public void key(){
        // ---------- SPACE ---------------
        Action space = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

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
    }

    public void update(){
        if(running){
        if((System.nanoTime()-lastAction)/1000000000.0>5&&start){
            start=false;
            level++;

            enem.increase();
            //mapsize = mapsize + mapsize / 4;
            //addMeteor();

            scaler =1.0/((double)mapsize/(800));
            offsetx=(int)(-400/scaler);
            offsety=0;
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
        int enemy=0;
        /**
        for(int x=0;x<threat.length;x++){
            for(int y=0;y<threat[0].length;y++){
                threat[x][y]=0;
            }
        }
        for(int x=0;x<threat2.length;x++){
            for(int y=0;y<threat2[0].length;y++){
                threat2[x][y]=0;
            }
        }

         **/



        for (Iterator<Unit> iterator = objects.iterator(); iterator.hasNext(); ) {

            Unit b = iterator.next();

            /**
            for(int x=0;x<threat.length;x++){
                for(int y=0;y<threat[0].length;y++){
                    grid[x][y].add(b);
                }
            }

            if(b.kind==2) {
                if (b.x < mapsize && b.x > 0 && b.y < mapsize / 2 && b.y > -mapsize / 2)
                    threat[(int) ((double) b.x / mapsize * 20)][(int) (((double) b.y + mapsize / 2) / mapsize * 20)]++;
            } else if(b.kind==1){
                if (b.x < mapsize && b.x > 0 && b.y < mapsize / 2 && b.y > -mapsize / 2)
                    threat2[(int) ((double) b.x / mapsize * 20)][(int) (((double) b.y + mapsize / 2) / mapsize * 20)]++;
            }

             **/


            if(!b.alive) {
                iterator.remove();



                        switch (b.kind){
                            case 1:
                                for (int y = 0; y < 10; y++)
                                fx.add(new Particles(Color.BLUE, (int) (Math.random() * 5) + 1, b.x, b.y, (int) (Math.random()*3), Math.random() * Math.toRadians(360), ((b.power/2 * Math.random())+b.power/2)*2,0));
                                break;
                            case 2:
                                for (int y = 0; y < 10; y++)
                                fx.add(new Particles(Color.RED, (int) (Math.random() * 5) + 1, b.x, b.y, (int) (Math.random()*3), Math.random() * Math.toRadians(360), ((b.power/2 * Math.random())+b.power/2)*2,0));
                                break;
                            case 3:
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

            }
            if(b.kind==1) {
                if (b instanceof TTT) {
                    ttt++;
                } else if (b instanceof SSS) {
                    sss++;
                } else if (b instanceof WWW) {
                    www++;
                }
            } else if(b.kind==2){
                enemy++;
            }
        }
        user.TTT=ttt;
        user.SSS=sss;
        user.WWW=www;

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
            lastAction=System.nanoTime();
        }
        }

    }

    public void addMeteor(){
        for(int x=0;x<100;x++){
            int sizerandom=0;
            int spawnx=0;
            int spawny=0;
            boolean inSpawnzone=false;
            do{
                sizerandom=(int)(Math.random()*100)+20;
                spawnx=(int)(Math.random()*(mapsize+mapsize/4));
                spawny=(int)(Math.random()*(mapsize+mapsize/2))-mapsize/4-mapsize/2;
                inSpawnzone=collision(spawnx-sizerandom/2,spawny-sizerandom/2,sizerandom,sizerandom,0,-mapsize/2,mapsize,mapsize);

            } while(inSpawnzone);

            objects.add(new Asteroid(spawnx,spawny,sizerandom,sizerandom,objects,lasers));;
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
            g.drawRect(0, -safebase / 2, safebase, safebase);
            g.drawRect(0, -mapsize / 2, mapsize, mapsize); //draws map
            g.drawRect(0, -mapsize / 2 - mapsize / 4, mapsize + mapsize / 4, mapsize + mapsize / 2); // draws meteor spawn area
            g.drawRect(0, -mapsize / 2 - mapsize / 4 - mapsize / 8, mapsize + mapsize / 4 + mapsize / 8, mapsize + mapsize / 2 + mapsize / 4); // draws spawn area
            g.setColor(Color.WHITE);
            /**
            a.drawRect(0, -safebase / 2, safebase, safebase);
            a.drawRect(0, -mapsize / 2, mapsize, mapsize); //draws map
            a.drawRect(0, -mapsize / 2 - mapsize / 4, mapsize + mapsize / 4, mapsize + mapsize / 2); // draws meteor spawn area
            a.drawRect(0, -mapsize / 2 - mapsize / 4 - mapsize / 8, mapsize + mapsize / 4 + mapsize / 8, mapsize + mapsize / 2 + mapsize / 4); // draws spawn area


            for(int x=0;x<threat.length;x++){
                for(int y=0;y<threat[0].length;y++){
                    g.drawRect(200+x*80,y*80,80,80);
                }
            }
             **/
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
        //undoes the transformation
        a.translate(-(offsetx),-(offsety));
        a.scale(1.0/scaler,1.0/scaler);
        a.translate(-400,-400);
        a.translate(-200,0);


        /**
        a.setColor(Color.GRAY);
        for(int x=0;x<threat.length;x++){
            for(int y=0;y<threat[0].length;y++){
                a.setColor(Color.GRAY);
                g.drawRect(200+x*800/threat.length,y*800/threat.length,800/threat.length,800/threat.length);
                if(threat[x][y]!=0||threat2[x][y]!=0) {
                    if (threat[x][y] > threat2[x][y]) {
                        g.setColor(Color.blue);
                        g.drawString(threat[x][y] + "", 200 + 400/threat.length + x * 800/threat.length, 400/threat.length + y * 800/threat.length);

                    } else {
                        g.setColor(Color.RED);
                        g.drawString(threat2[x][y] + "", 200 + 400/threat.length + x * 800/threat.length, 400/threat.length + y * 800/threat.length);
                    }
                } else {
                    g.drawString(threat[x][y] + "", 200 + x * 800/threat.length +400/threat.length, 400/threat.length + y * 800/threat.length);
                }


            }
        }
         **/


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
        g.drawString("<< ALPHA TEST v.07 >>",center(200,"<< ALPHA TEST v.07 >>",g),60);

        g.drawString("^^^ : "+user.moolah,10,130);
        g.drawString("wave : " +(level-1),10,150);
        //g.drawString("size: "+objects.size(),10,180);

        g.setColor(Color.WHITE);
        g.drawRect(0, 175, 200, 50);

        if(selection==1)
            g.drawRect(5, 180, 190, 40);
        if(user.moolah<user.TTTCost)
            g.setColor(Color.DARK_GRAY);

        g.drawString(" ^^^ : "+user.TTTCost+" | TTT",10,200);

        g.setColor(Color.WHITE);
        g.drawRect(0,225,200,50);
        if(selection==2)
            g.drawRect(5,230,190,40);
        if((user.moolah<user.SSSCost&&user.unlockedSSS)||(user.moolah<100&&!user.unlockedSSS))
            g.setColor(Color.DARK_GRAY);

        if(user.unlockedSSS){
            g.drawString(" ^^^ : "+user.SSSCost+" | SSS",10,250);
        } else{
            g.drawString(" ^^^ : 100 | UNLOCK SSS ",10,250);
        }

        g.setColor(Color.WHITE);
        g.drawRect(0,275,200,50);
        if(selection==3)
            g.drawRect(5,280,190,40);
        if((user.moolah<user.WWWCost&&user.unlockedWWW)||(user.moolah<200&&!user.unlockedWWW))
            g.setColor(Color.DARK_GRAY);

        if(user.unlockedWWW){
            g.drawString(" ^^^ : "+user.WWWCost+" | WWW",10,300);
        } else{
            g.drawString(" ^^^ : 200 | UNLOCK WWW ",10,300);
        }


        g.setColor(Color.WHITE);
        g.drawRect(0,325,200,50);
        if(selection==4)
            g.drawRect(5,330,190,40);
        if((user.moolah<user.EEECost&&user.unlockedEEE)||(user.moolah<200&&!user.unlockedEEE))
            g.setColor(Color.DARK_GRAY);

        if(user.unlockedEEE){
            g.drawString(" ^^^ : "+user.EEECost+" | EEE",10,350);
        } else{
            g.drawString(" ^^^ : 500 | UNLOCK EEE ",10,350);
        }

        g.setColor(Color.WHITE);
        g.drawRect(0,500,200,300);
        if(selection==1){
            g.drawString("TTT",80,530);
            g.drawRect(10,560,100,30);
            g.drawString((user.strategyTTT)?"OFFENSE":"DEFENSE",20,580);
            g.drawString("Select Target",20,630);
            g.drawRect(10,610,100,30);

        } else if(selection==2){
            g.drawString("SSS",80,530);
            g.drawString((user.strategySSS)?"OFFENSE":"DEFENSE",20,580);
        } else if(selection==3){
            g.drawString("WWW",80,530);
            g.drawString((user.strategyWWW)?"OFFENSE":"DEFENSE",20,580);
        } else if(selection==4){
            g.drawString("EEE",80,530);
            g.drawString("mining ship.",60,580);
        }

        if(!running)
        g.drawString("Paused.",200+center(800,"Paused.",g),400);



        //g.drawRect(700,40,25,25);
        //g.fillRect(705,45,5,15);
        //g.fillRect(715,45,5,15);


    }

    public void drawDev(Graphics g){
        g.drawString("DEVELOPER VERSION",220,40);
        g.drawString("units: "+objects.size(),220,60);
        g.drawString("lasers: "+lasers.size(),220,80);
        g.drawString("particles: "+fx.size(),220,100);
        g.drawString("mapsize: "+mapsize+"m",220,120);





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

            if(collision(e.getX(),e.getY(),10,560,100,30)){
                user.strategyTTT=!user.strategyTTT;
            }
            if(collision(e.getX(),e.getY(),10,610,100,30)){
                selection=-1;
                System.out.println("select target");
            }

        } else if (e.getX()>225){
            //variable that determines whether or not there is another block already beneath the cursor
            boolean cheaterprevention=false;
            //checks the position of all the objects in the array and makes sure it's not under the cursor
            for(int x=0;x<objects.size();x++){
                if(objects.get(x)instanceof Spawner||objects.get(x).kind==3){ //only considers objects that are spawners or asteroids
                    //bad way to check all 4 corners
                    if(collision(worldX-25,worldY-25,objects.get(x).x,objects.get(x).y,objects.get(x).w,objects.get(x).h)||collision(worldX+25,worldY-25,objects.get(x).x,objects.get(x).y,objects.get(x).w,objects.get(x).h)||collision(worldX-25,worldY+25,objects.get(x).x,objects.get(x).y,objects.get(x).w,objects.get(x).h)||collision(worldX+25,worldY+25,objects.get(x).x,objects.get(x).y,objects.get(x).w,objects.get(x).h)){
                        cheaterprevention=true;//says there is an object beneath the cursor
                    }
                }
            }

            if(!cheaterprevention){
                switch (selection){
                    case -1:
                        for(int x=0;x<objects.size();x++){
                            if(collision(worldX,worldY, objects.get(x).x-25, objects.get(x).y-25, 50,50)){
                                user.targeti=x;
                                selection=0;
                            }
                        }

                        break;
                    case 1:
                        //if(user.moolah>=user.TTTCost) {
                            objects.add(new TTTSpawner(worldX, worldY, 1, objects, lasers));
                            //user.TTT++;
                            user.moolah -= user.TTTCost;
                            user.TTTCost += 5;
                        //}
                            selection = 0;

                        break;
                    case 2:
                        //if(user.moolah>=user.SSSCost) {
                            objects.add(new SSSSpawner(worldX, worldY, 1, objects, lasers));
                            //user.SSS++;
                            user.moolah -= user.SSSCost;
                            user.SSSCost += 5;
                        //}
                            selection = 0;

                        break;
                    case 3:
                        //if(user.moolah>=user.WWWCost) {
                            objects.add(new WWWSpawner(worldX, worldY, 1, objects, lasers));
                            //user.WWW++;
                            user.moolah -= user.WWWCost;
                            user.WWWCost += 5;
                        //}
                        selection=0;
                        break;
                    case 4:
                        objects.add(new EEESpawner(worldX, worldY, 1, objects, lasers));
                        //user.WWW++;
                        user.moolah -= user.EEECost;
                        user.EEECost *= 2;
                        //}
                        selection=0;
                        break;
                }
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        if(selection==0) {
            startx = e.getX();
            starty = e.getY();
        }

    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
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
        if(selection==0) {
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
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
