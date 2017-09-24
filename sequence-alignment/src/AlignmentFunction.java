public class AlignmentFunction {

    static Result[][] align(final String s1, final String s2,
			    StringBuilder s1_aligned, StringBuilder s2_aligned) {
	    // put your solution here
        Result[][] res = new Result[s2.length()+1][s1.length()+1];
        res[0][0] = new Result(0, Mutation.M);

        for(int j = 1; j<=s1.length(); j++){
            res[0][j] = new Result( -j, Mutation.I);
        }

        for(int i = 1; i<=s2.length(); i++){
            res[i][0] = new Result(-i, Mutation.D);
        }

        for(int i=1; i<=s2.length(); i++) {
            for (int j = 1; j <= s1.length(); j++) {
                int score = Driver.match(s2.charAt(i-1), s1.charAt(j-1));
                int mx = max(res[i-1][j-1].score+score, res[i-1][j].score-1, res[i][j-1].score-1);
                Result aRes = null;
                if(res[i-1][j-1].score+score == mx){
                    aRes = new Result(mx, Mutation.M);
                }else if(res[i-1][j].score-1 == mx){
                    aRes = new Result(mx, Mutation.I);
                }else{
                    aRes = new Result(mx, Mutation.D);
                }
                res[i][j] = aRes;
            }
        }

        int i = s2.length(), j =s1.length();
        while(i > 0 || j > 0) {
            if (i > 0 && res[i][j].dir == Mutation.I) {
                s1_aligned.append('_');
                s2_aligned.append(s2.charAt(i - 1));
                i--;
            } else if (i > 0 && j > 0 && res[i][j].dir == Mutation.M) {
                s2_aligned.append(s2.charAt(i - 1));
                s1_aligned.append(s1.charAt(j - 1));
                i--;
                j--;
            } else if(j > 0 &&  res[i][j].dir == Mutation.D){//res[i][j].dir == Mutation.D
                s2_aligned.append('_');
                s1_aligned.append(s1.charAt(j - 1));
                j--;
            }else{
                break;
            }
        }

        if(i==0){
            while(j > 0){
                s1_aligned.append(s1.charAt(j - 1));
                s2_aligned.append('_');
                j--;
            }
        }else if(j == 0){
            while(i > 0){
                s1_aligned.append('_');
                s2_aligned.append(s2.charAt(i - 1));
                i--;
            }
        }



        return res;
    }
    public static int max(int a, int b, int c){
        int tmp = a > b ? a : b;
        return tmp > c? tmp : c;
    }

    public static void main(String[] args)
    {
       System.out.println( Driver.match('_','A'));
    }
}
