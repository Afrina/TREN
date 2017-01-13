package extend.ClusterDataSet;

import org.apache.commons.math3.linear.RealVector;

public class SimilarityMatrics {
	 public double FindCosineSimilarity(RealVector v1, RealVector v2)
     {
		 double dotProduct = v1.dotProduct(v2);
//			System.out.println( "Dot: " + dotProduct);
//			System.out.println( "V1_norm: " + v1.getNorm() + ", V2_norm: " + v2.getNorm() );
		double normalization = (v1.getNorm() * v2.getNorm());
			//System.out.println( "Norm: " + normalization);
			double result =  dotProduct / normalization;
         
             return result;
     }

//#endregion

     public float DotProduct(double[] vecA, double[] vecB)
     {

         float dotProduct = 0;
         for (int i = 0; i < vecA.length; i++)
         {
             dotProduct += (vecA[i] * vecB[i]);
         }

         return dotProduct;
     }

     // Magnitude of the vector is the square root of the dot product of the vector with itself.
//     public static float Magnitude(float[] vector)
//     {
//         return (float)Math.Sqrt(DotProduct(vector, vector));
//     }



    // #region Euclidean Distance
     //Computes the similarity between two documents as the distance between their point representations. Is translation invariant.
//     public static float FindEuclideanDistance(int[] vecA, int[] vecB)
//     {
//         float euclideanDistance = 0;
//         for (var i = 0; i < vecA.Length; i++)
//         {
//             euclideanDistance += (float)Math.Pow((vecA[i] - vecB[i]), 2);
//         }
//
//         return (float)Math.Sqrt(euclideanDistance);
//
//     }
    // #endregion

     //#region Extended Jaccard
     //Combines properties of both cosine similarity and Euclidean distance
//     public static float FindExtendedJaccard(float[] vecA, float[] vecB)
//     {
//         var dotProduct = DotProduct(vecA, vecB);
//         var magnitudeOfA = Magnitude(vecA);
//         var magnitudeOfB = Magnitude(vecB);
//
//         return dotProduct / (magnitudeOfA + magnitudeOfB - dotProduct);
//
//     }
}
