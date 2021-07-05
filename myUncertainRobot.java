import world.World;
import java.awt.Point;
import java.util.ArrayList;
import world.Robot;

public class myUncertainRobot extends Robot {
    private World terra;

    @Override
    public void travelToDestination() {
        // start off with start node
        String[][] mem = new String[terra.numRows()][terra.numCols()];
        for(int i = 0; i < mem.length;i++){
            for(int j = 0; j < mem[i].length;j++){
                mem[i][j] = "0";
            }
        }
        boolean finished = true;
        ArrayList<Point> endPs = new ArrayList<Point>();
        while(finished){
            finished = false;
            Point endP = terra.getEndPos();
            node start = new node(super.getPosition(), null, endP);
            ArrayList<node> openL = new ArrayList<node>();
            ArrayList<node> closedL = new ArrayList<node>();
            ArrayList<Point> wall = new ArrayList<Point>();
            ArrayList<Point> visited = new ArrayList<Point>();
            
            openL.add(start);
            node fin = new node(start.pos, null, endP);
            ArrayList<Point> path = new ArrayList<Point>();
            // System.out.println(endP.getX() + " " + endP.getY());
            boolean found = false;
            while (!openL.isEmpty() && !found) {
                node temp = openL.get(openL.size() - 1);
                for (int i = openL.size() - 1; i >= 0; i--) {
                    if (temp.totDist > openL.get(i).totDist) {
                        temp = openL.get(i);
                    }
                    // System.out.println("Total DIst : " + openL.get(i).totDist + " x " + openL.get(i).pos.getX() + " Y "
                    //         + openL.get(i).pos.getY());
                }
                // System.out.println(" ");
                openL.remove(temp);
                // if(mem[(int)temp.pos.getX()][(int)temp.pos.getY()].equals("X")){
                //     continue;
                // }
                if (wall.contains(temp.pos)) {
                    continue;
                }
                
                else if (temp.pos.equals(endP)) {
                    // found end
                    fin = temp;
                    found = true;
                    break;
                } else if (super.pingMap(temp.pos).equals("X")) {
                    mem[(int)temp.pos.getX()][(int)temp.pos.getY()] = "X";
                    wall.add(temp.pos);
                    continue;
                }
                node[] node8;
                Point tempP = new Point(temp.pos);
                node8 = getAround(tempP, temp, endP);
                //System.out.println(temp.pos.getX() + " " + temp.pos.getY() + " | " + temp.totDist);
                //System.out.println(" ");

                for (int i = 0; i < node8.length; i++) {
                    node t = node8[i];
                    if (wall.contains(t.pos)) {
                        continue;
                    }
                    if (visited.contains(t.pos)) {
                        inOpen(t, openL);
                        inClosed(t, closedL, openL);
                        continue;
                    }
                    if (t.pos.getX() < 0 || t.pos.getX() > terra.numRows() - 1) { // outofbounds
                        continue;
                    } else if (t.pos.getY() < 0 || t.pos.getY() > terra.numCols() - 1) { // outofbounds
                        continue;
                    } else {
                        visited.add(t.pos);
                        openL.add(t);
                    }
                }
                closedL.add(temp);
            }

            while (fin.parent != null) {
                path.add(fin.pos);
                fin = fin.parent;
            }
            if(path.size()>5){
                for(int i = path.size()-1; i > path.size()-6;i--){
                    super.move(path.get(i));
                }
            }
            else{
            for (int i = path.size() - 1; i >= 0; i--) {
                Point first = super.getPosition();
                super.move(path.get(i));

                if(first.equals(super.getPosition())){
                    finished = true;
                    break;
                }
            }
            
        }
        if(!super.getPosition().equals(endP)){
            finished = true;
        }
        if(path.size()>0){
            endPs.add(path.get(path.size()-1));
            if(endPs.contains(path.get(path.size()-1))){
                finished = false;
            }
        }
        }
    }

    public boolean inOpen(node temp, ArrayList<node> op) {
        for (node n : op) {
            if (temp.pos.equals(n.pos)) {
                if (temp.totDist < n.totDist) {
                    op.remove(n);
                    op.add(temp);
                }
                return true;
            }
        }
        return false;
    }

    public boolean inClosed(node temp, ArrayList<node> closed, ArrayList<node> op) {
        for (node n : closed) {
            if (temp.pos.equals(n.pos)) {
                if (temp.totDist < n.totDist) {
                    op.add(temp);
                }
                return true;
            }
        }
        return false;
    }

    public node[] getAround(Point tempP, node parent, Point endP) {
        node[] temp = new node[8];
        double x = tempP.getX();
        double y = tempP.getY();
        Point tp = new Point();
        tp.setLocation(x, y + 1);
        temp[0] = new node(new Point((int) x, (int) y + 1), parent, endP);
        tp.setLocation(x + 1, y + 1);
        temp[1] = new node(new Point((int) x + 1, (int) y + 1), parent, endP);
        tp.setLocation(x + 1, y);
        temp[2] = new node(new Point((int) x + 1, (int) y), parent, endP);
        tp.setLocation(x + 1, y - 1);
        temp[3] = new node(new Point((int) x + 1, (int) y - 1), parent, endP);
        tp.setLocation(x, y - 1);
        temp[4] = new node(new Point((int) x, (int) y - 1), parent, endP);
        tp.setLocation(x - 1, y - 1);
        temp[5] = new node(new Point((int) x - 1, (int) y - 1), parent, endP);
        tp.setLocation(x - 1, y);
        temp[6] = new node(new Point((int) x - 1, (int) y), parent, endP);
        tp.setLocation(x - 1, y + 1);
        temp[7] = new node(new Point((int) x - 1, (int) y + 1), parent, endP);

        return temp;
    }

    public void setWorld(World w) {
        terra = w;
    }

    public double heuristic(Point currPoint, Point endP) {
        double x = currPoint.getX();
        double y = currPoint.getY();
        double guess = Math.max(Math.abs(x - endP.getX()), Math.abs(y - endP.getY()));
        return guess;
    }

    class node {
        double heurG;
        double fromStart;
        double totDist;
        Point pos;
        node parent;

        public node(Point loc, node par, Point endP) {
            if (loc != null) {
                heurG = heuristic(loc, endP);
            }
            parent = par;
            pos = loc;
            if (parent == null)
                fromStart = 0;
            else
                fromStart = parent.fromStart + 1;
            totDist = heurG + fromStart;
        }
    }

    public static void main(String[] args) {
        String filepath = "";
        if (args.length > 0) {
            filepath = args[0];
        }
        try {
            World myWorld = new World(filepath, true);
            myUncertainRobot rob = new myUncertainRobot();
            rob.addToWorld(myWorld);
            rob.setWorld(myWorld);
            rob.travelToDestination();
            System.out.println(rob.getPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}