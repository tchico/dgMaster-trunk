package generator.misc;

/**
*
 */
import cern.jet.random.Uniform;

/**
 * <h2>Discrete Probability Distribution Function</h2>
 * Generate numbers according to a discrete PDF.
 */
public class PDFDiscrete
{
    /** The discrete probability distribution */
    private double []pk;

    /**
     * Generate a number according to the given PDF.
     * Numbers start at 0 and end at PDF.length-1.
     * @return A number generated according to the PDF.
     */
    public int next()
    {
        int    pos;
        double sum, ran;
        
        // Pick a random index according to the PDF
        ran     = Uniform.staticNextDouble();
        pos     = 0;
        sum     = this.pk[0];
        //sum = 0 ;
        //System.out.println(" sum : " + sum ) ;
        while ( (sum < ran) && (pos < pk.length))
        {
           // sum += this.pk[pos];
        	//System.out.println(" pk : " + this.pk[pos] ) ;
        	pos++;
        	sum += this.pk[pos];
            
            // // pos++;
            //System.out.println("Ran : " + ran + " Pos : " + pos + " sum : " + sum ) ;
        }
        //pos-- ;
        // When running out of indices. Take the last one .     
        if (pos == pk.length) pos = pk.length-1;
        
        return(pos);
    }

    /**
     * Make a discrete probability function.
     * @param pk The PDF. Make sure the sum of the elements equals 1.
     */
    public PDFDiscrete(double []pk)
    {
       this.pk = pk;
    }
}
