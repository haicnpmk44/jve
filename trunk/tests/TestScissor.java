import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Processor;
import javax.media.Time;

import video.util.Scissors;


public class TestScissor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String sin = "file://home/thiago/MeusDocumentos/ufcg/smultimidia/clip02.mpg";
		String sout = "file://home/thiago/MeusDocumentos/ufcg/smultimidia/s.mpg";

		MediaLocator in = new MediaLocator(sin);
		MediaLocator out = new MediaLocator(sout);

		long[] s ={20000 };
		long[] e ={ new Long("2200000000") };




		Scissors t = new Scissors(in,out,s,e);
		try {
//			Processor p = Manager.createProcessor(in);
//			Time ti = p.getDuration();
//			System.out.println(ti.getSeconds());
//			System.out.println(ti.getNanoseconds());
//			System.out.println(ti.getNanoseconds()/ti.getSeconds());
			t.cut();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		System.exit(0);


	}

}
