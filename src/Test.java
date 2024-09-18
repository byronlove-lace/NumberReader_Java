import java.util.Scanner;

class Test
{
        public static int[] sortMe = {2, 8, 5, 9, 4};

        public static void sort()
        {
                for (int i = 1; i < sortMe.length; ++i)
                {
                        int j = i - 1;        

                        if (sortMe[i] > sortMe[j])
                        {
                                int temp = sortMe[j];
                                sortMe[j] = sortMe[i];
                                sortMe[i] = temp;
                        }
                }
        }

        public static void main(String args[])
        {
                sort();
                for (int i : sortMe)
                        System.out.println(i);
        }
}
