import java.lang.*;
import java.io.*;
import java.util.*;

class matrix
{
	public int size;
	public int check=1;
	public List<List<Integer>> list = new ArrayList<List<Integer>>();
	
	public void storeInput(String filename)
	{
		int i;
		Fileread obj = new Fileread();
		List<String> records = obj.readFile(filename);
		size = Integer.parseInt(records.get(0));
		if(size == 0)
		{
			System.out.println("Invalid Input");
			return;
		}
		for(i=1; i<3; i++)
		{
			String s = records.get(i);
			List<Integer> sum = new ArrayList<Integer>();
			String[] arr = s.replaceAll("^[,\\s]+", "").split("[,\\s]+");

			try{
				for(int c=0;c<size;c++)
					sum.add(Integer.parseInt(arr[c]));
				}
				catch(Exception e)
				{
					System.out.println("Invalid input");
					return;
				}

			list.add(sum);
		}
	}

	/*prints 0 if check=0 or computes the matrix and prints it otherwise*/
	public void printOutput()
	{
		int[][] printMatrix = null;
		if(check == 1)
			printMatrix = computeMatrix();		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) 
			{
				bw.write(check + "\n");
				if(check==1)
				{
					for(int i=0; i<size; i++)
						{
							int j;
							for(j=0;j<size-1; j++)
								{
									bw.write(printMatrix[i][j] + ",");
								}
							bw.write(printMatrix[i][j] + "\n");
						}
				} 	
			} 
		catch(IOException e)
			{
				e.printStackTrace();
			}
	}

	/*checks if there's a possible matrix*/
	public void checker()
	{
		int[] sum = new int[2];
		int[] max = {0,0};
		int[] nonZero = {0,0};
		for(int i=0; i<2; i++)
		{
			sum[i]=0;
			List<Integer> dummyList = list.get(i); 
			for(int j=0; j<size; j++)
			{
				int dummy = dummyList.get(j);
				sum[i] = sum[i] + dummy;
				
				if(dummy!=0)
					nonZero[i]++;
		
				if(dummy>max[i])
					max[i]=dummy;
	
			}
		}
		if(check == 1)
		{
			if( ( (sum[0]- sum[1]) != 0 ) || max[0]>nonZero[1] || max[1]>nonZero[0])
				check =0;
		}
	}
	
	public void sortList(List<Pair> list1)
	{
		Collections.sort(list1, new Comparator<Pair>(){
                @Override
                public int compare(Pair lhs, Pair rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getKey() > rhs.getKey() ? -1 : (lhs.getKey() < rhs.getKey() ) ? 1 : 0;
                }
            });
	}
	public int[][] computeMatrix()
	{
		int[][] ans = new int[size][size];
		List<Pair> rowSumState = new ArrayList<Pair>(); 
		List<Pair> colSumState = new ArrayList<Pair>();
		
		for(int i=0; i<size; i++)
		{
			for(int j=0;j<size; j++)
				ans[i][j] = 0;
		}

		for(int i=0; i<size; i++)
			{
				Pair p1 = new Pair(list.get(0).get(i),i);
				Pair p2 = new Pair(list.get(1).get(i),i);
				rowSumState.add(p1);
				colSumState.add(p2);
			}
		sortList(rowSumState);

		//greedy method
		for (int i=0; i<size ;i++ )
		{
			sortList(colSumState);
			int j=0;
			while(j<rowSumState.get(i).getKey())
			{
				colSumState.get(j).setKey();
				ans[rowSumState.get(i).getValue()][colSumState.get(j).getValue()] = 1;
				j++;
			}			
		}

		return ans;
	}

	public static void main(String args[])
	{
		matrix obj = new matrix();
		obj.storeInput("input.txt");
		obj.checker();
		obj.printOutput();
	}

}
