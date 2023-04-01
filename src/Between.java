import java.util.Arrays;
import java.util.LinkedList;

public class Between extends GraphWtAL{

    private int[] minDist; // minimum distance to each vertex
    private int[] numParents; // number of parents for each vertex
    private float[] interimBtwn; // interim betweenness calculations
    public float[] btwn; // final betweenness scores
    public Between(int size) {
        super(size);
        minDist = new int[size];
        numParents = new int[size];
        interimBtwn = new float[size];
        btwn = new float[size];
        Arrays.fill(btwn, 0);
    }

    public void betFind(){

        for(int i=0;i<n;i++) {
            //Reset edge mark
            edgeReset(-1);
            //
            modBfs(i);
            //Refresh interim betweeness
            for(int j=0;j<n;j++) {
                interimBtwn[j] = 0;
            }
            //
            bpost(i);

            //add interim betweeness to overall betweeness
            for(int j=0;j<n;j++) {
                //System.out.println("Current "+btwn[j]+" Interim Value "+interimBtwn[j]);
                btwn[j] = btwn[j]+ interimBtwn[j];
            }

        }

    }

    public float bpost( int s){
        if(interimBtwn[s]==0){
            interimBtwn[s]=1;
            for(GNode list=AdjList[s];list!=null;list=list.next) {
                if(list.mark ==1){
                    interimBtwn[s] = interimBtwn[s] + bpost(list.nbr);
                }
            }
        }
        return interimBtwn[s]/numParents[s];
    }

    protected void modBfs(int startVert){
        //initialize all vertex to unmarked
        //this.reset(0);
        for(int i=0;i<n;i++) {
            minDist[i] = -1;
            numParents[i] =1;
        }
        //Create an empty queue for discovered vertexes
        LinkedList<Integer> queue = new LinkedList<Integer>();
        //Add source vertex to queue
        queue.add(startVert);
        //Mark source vertex as visited
        mark[startVert]=1;
        minDist[startVert]=0;
        while(queue.size()!=0) {
            GNode currVert;
            // Dequeue the first vertex from the queue
            startVert = queue.poll();
            //Loop through adjacency list to get vertex to the selected vertex
            for(GNode list=AdjList[startVert];list!=null;list=list.next) {
                //Get the next connected vertex
                currVert = list;
                //check if vertex has been marked visited
                if(minDist[currVert.nbr]==-1){
                    //Mark vertex as visited
                    minDist[currVert.nbr] = minDist[startVert] +1;
                    //enqueue vertex
                    queue.add(currVert.nbr);
                    currVert.mark=1;
                }else if(minDist[currVert.nbr] == minDist[startVert] +1){
                    currVert.mark=1;
                    numParents[currVert.nbr] = numParents[currVert.nbr]+1;
                }
            }
        }
        //return false;
    }
    public String toString()
    {
        StringBuilder result = new StringBuilder(super.toString());
        result.append("\n");
        for (int i = 0; i < n; i++) {
            result.append(String.format("%.2f", btwn[i]));
            result.append("\t");
        }
        return result.toString();
    }
}
