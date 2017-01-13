package extend.ClusterDataSet;

import java.awt.GradientPaint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.math3.linear.RealVector;

public class DocumentClustering {
	public static int globalCounter;
	public static int counter;

	public DocumentClustering(){

	}
	public ArrayList<Centroid> PrepareDocumentCluster(int k, ArrayList<RealVector> documentCollection) throws IOException
	{
		globalCounter = 0;
		//prepares k initial centroid and assign one object randomly to each centroid
		ArrayList<Centroid> centroidCollection = new ArrayList<Centroid>();
		Centroid c;

		/*
		 * Avoid repeation of random number, if same no is generated more than once same document is added to the next cluster 
		 * so avoid it using HasSet collection
		 */
		HashSet<Integer> uniqRand = new HashSet<Integer>();
		uniqRand = GenerateRandomNumber(uniqRand,k,documentCollection.size());
		System.out.println("got size"+uniqRand);
		for(int pos : uniqRand) 
		{
			
			c = new Centroid();                
			c.groupedDocument = new ArrayList<RealVector>();
			if(pos==documentCollection.size()){c.center = documentCollection.get(pos-1);}
			else{c.center = documentCollection.get(pos);}
			centroidCollection.add(c);                
		}
		System.out.println(centroidCollection.size());
		Boolean stoppingCriteria;
		ArrayList<Centroid> resultSet = new ArrayList<Centroid>();
		ArrayList<Centroid> prevClusterCenter = new ArrayList<Centroid>();

		resultSet = InitializeClusterCentroid(resultSet, centroidCollection.size());

		do
		{
			File f = new File("weka\\weka-data1.txt");
			BufferedWriter output = new BufferedWriter(new FileWriter(f,true));;
			prevClusterCenter = new ArrayList<Centroid>();
			System.out.println("again here");
			for(Centroid cen: centroidCollection){
				prevClusterCenter.add(cen);
			}
			
			
			System.out.println("again here R PO"+centroidCollection.size());
			for(int g=0;g< centroidCollection.size();g++){
				resultSet.get(g).center = centroidCollection.get(g).center;
			}
			for(RealVector obj : documentCollection)
			{
				int index = FindClosestClusterCenter(centroidCollection, obj);
				resultSet.get(index).groupedDocument.add(obj);
				//resultSet.get(index).center = centroidCollection.get(index).center;
			}
			for(int i=0;i<resultSet.size();i++){
				System.out.println("internal "+i+" "+resultSet.get(i).groupedDocument.size());
			}
			System.out.println("again here bef ini"+centroidCollection.size());
			centroidCollection = InitializeClusterCentroid(centroidCollection, centroidCollection.size());
			centroidCollection = CalculateMeanPoints(resultSet);
			
			for(int l = 0;l<centroidCollection.size();l++){
				for(int x=0; x<centroidCollection.get(l).center.toArray().length;x++){
					output.write(centroidCollection.get(l).center.getEntry(x)+",");
					//output.write("cen"+centroidCollection.get(l).center.toArray().length+",");
					output.newLine();
					output.write(prevClusterCenter.get(l).center.getEntry(x)+",");
				}
				output.newLine();  
				  }
				output.newLine();
			output.close();
			stoppingCriteria = CheckStoppingCriteria(prevClusterCenter, centroidCollection);
			if (!stoppingCriteria)
			{   
				//initialize the result set for next iteration
				resultSet = InitializeClusterCentroid(resultSet, centroidCollection.size());
				for(int i=0;i<resultSet.size();i++){
					System.out.println("before internal "+i+" "+resultSet.get(i).groupedDocument.size());
				}
			}


		}while (stoppingCriteria == false);

	//	_counter = counter;
		for(int i=0;i<resultSet.size();i++){
			System.out.println(resultSet.get(i).groupedDocument.size());
		}
		return resultSet;

	}
	public HashSet<Integer> GenerateRandomNumber(HashSet<Integer> uniqRand, int k, int docCount)
	{
		Random r = new Random();
		if (k > docCount)
		{
			do
			{
				int pos = 0 + (int)(Math.random() * ((docCount - 0) + 1));
				uniqRand.add(pos);
			} while (uniqRand.size() != docCount);
		}            
		else
		{
			do
			{
				int pos = 0 + (int)(Math.random() * ((docCount - 0) + 1));
				uniqRand.add(pos);
			} while (uniqRand.size() != k);
		}
		return uniqRand;
	}

	public ArrayList<Centroid> InitializeClusterCentroid(ArrayList<Centroid> clusterSet,int numberOfClusters)
	{
		Centroid c;
		clusterSet = new ArrayList<Centroid>();
		for (int i = 0; i <numberOfClusters; i++)
		{
			c = new Centroid();
			c.groupedDocument = new ArrayList<RealVector>();
			clusterSet.add(c);
		}
		return clusterSet;
	}

	public int FindClosestClusterCenter(ArrayList<Centroid> centroidCollection,RealVector obj)
	{ 
		double[] similarityMeasure = new double[centroidCollection.size()];
		System.out.println("came for debugging"+centroidCollection.size());
		for (int i = 0; i < centroidCollection.size(); i++)
		{
			//System.out.println("debugging"+centroidCollection.get(i).groupedDocument.size());
			//if(centroidCollection.get(i).groupedDocument.size()>0){
				similarityMeasure[i] = new SimilarityMatrics().FindCosineSimilarity(centroidCollection.get(i).center, obj); 
			//}
			

		}

		int index = 0;
		double maxValue = similarityMeasure[0];
		for (int i = 0; i < similarityMeasure.length; i++)
		{
			//if document is similar assign the document to the lowest index cluster center to avoid the long loop
			if (similarityMeasure[i] >maxValue)
			{
				maxValue = similarityMeasure[i];
				index = i;

			}
		}
		return index;

	}
	public ArrayList<Centroid> CalculateMeanPoints(ArrayList<Centroid> resultSet)
	{
		for(int i = 0; i < resultSet.size(); i++){
			ArrayList<Double> values = new ArrayList<Double>();
			if(resultSet.get(i).groupedDocument.size()> 0){
				for(int j = 0; j < resultSet.get(i).center.toArray().length; j++)
				{
					//System.out.println("for index "+j);
					double total = 0;
					for(int k = 0;k<resultSet.get(i).groupedDocument.size();k++)
					{
						//System.out.println(resultSet.get(i).groupedDocument.get(k));
						total = total + resultSet.get(i).groupedDocument.get(k).getEntry(j);
					}
					values.add(total/resultSet.get(i).groupedDocument.size());				
				}
				for(int x = 0; x < resultSet.get(i).center.toArray().length; x++)
				{
					resultSet.get(i).center.setEntry(x, values.get(x));
				}
			//	System.out.println(values);
			}
		}
		return resultSet;
	}
	  public Boolean CheckStoppingCriteria(ArrayList<Centroid> oldCentroidCollection, ArrayList<Centroid> newCentroidCollection)
	  {
          
          globalCounter++;
          counter = globalCounter;
          if (globalCounter > 500)
          {
        	  System.out.println("returning from global counter");
              return true;
          }
         
          else
          {
        	  System.out.println("not from global counter");
              Boolean stoppingCriteria = null;
              int[] clusterNumberChnage = new int[newCentroidCollection.size()];
              for(int k = 0;k<newCentroidCollection.size();k++){
            	  if(newCentroidCollection.get(k).groupedDocument.size() == oldCentroidCollection.get(k).groupedDocument.size()){
            		  clusterNumberChnage[k] = 0;
            	  }
            	  else{
            		  clusterNumberChnage[k] = 1;
            	  }
              }
              for(int k: clusterNumberChnage){
            	  if(clusterNumberChnage[k]==1){
            		  stoppingCriteria = false;
            		  break;
            	  }
            	  else{
            		  stoppingCriteria = true;
            	  }
              }
              if(stoppingCriteria) return true;
               int[] changeIndex = new int[newCentroidCollection.size()]; 
               int count=0;
              for (int j = 0; j < newCentroidCollection.size(); j++)
              {
                  for(int n=0;n<newCentroidCollection.get(j).center.toArray().length;n++){
                	  count = 0;
                	  if(newCentroidCollection.get(j).center.getEntry(n) == oldCentroidCollection.get(j).center.getEntry(n)){
                		  count++;
                	  }
                  }
                  if (count == newCentroidCollection.get(j).center.toArray().length)
                  {
                      changeIndex[j] = 0;
                  }
                  else
                  {
                      changeIndex[j] = 1;
                  }
            	 
              }
              for(int k:changeIndex){
            	  if(changeIndex[k]==1){
            		  stoppingCriteria = false;
            		  return stoppingCriteria;
            	  }
            	  else{
            		  stoppingCriteria = true;
            	  }
              }
              return stoppingCriteria;
              
          }
         
          
      }
}
