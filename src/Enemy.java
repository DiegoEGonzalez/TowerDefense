import java.util.*;
public class Enemy{
    int enemies=0;
    int level=0;
    boolean TTT=false;
    boolean SSS=false;
    boolean WWW=false;
    boolean spawning=false;
    ArrayList <Unit> objects;
    ArrayList <Laser> lasers;

    public Enemy(ArrayList <Unit> objects, ArrayList <Laser> lasers){
        this.objects=objects;
        this.lasers=lasers;
    }

    public void increase(){
        level++;
        Alpha.mapsize+=level*50;
        if(level>6){
            WWW=true;
        } else if(level>3){
            SSS=true;
        }
        spawning=true;
        enemies=(int)Math.pow(2,level);
    }

    public void spawn(){
        for(int x=0;x<enemies;x++){
            if(Math.random()<.002){
                int spawnxvariation = (int)(Math.random()*Alpha.mapsize/8);
                int spawnx = Alpha.mapsize+Alpha.mapsize/4+spawnxvariation;
                int spawny = (int)(Math.random()*Alpha.mapsize)-Alpha.mapsize/2;
                if(!SSS&&!WWW){
                    objects.add(new TTT(spawnx,spawny,2,objects,lasers));
                    enemies--;
                } else if(!WWW){
                    if(Math.random()<.8)
                        objects.add(new TTT(spawnx,spawny,2,objects,lasers));
                    else
                        objects.add(new SSS(spawnx,spawny,2,objects,lasers));
                    enemies--;
                } else {
                    if(Math.random()<.6)
                        objects.add(new TTT(spawnx,spawny,2,objects,lasers));
                    else
                    if(Math.random()<.6)
                        objects.add(new SSS(spawnx,spawny,2,objects,lasers));
                    else
                        objects.add(new WWW(spawnx,spawny,2,objects,lasers));
                    enemies--;
                }
            }

        }
        if(enemies==0){
            spawning=false;
        }
    }
}