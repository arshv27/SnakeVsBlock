/**
 * Inherits from Token class. Initializes an explosion image inside image view.
 *
 * @author Daksh Shah and Arsh Verma
 */

public class DestroyBlocks extends Tokens {
    protected  String path = "./../Images/icons8-explosion-96.png";

	/**
	 * @return Path of the token image in directory
	 */
    @Override
    protected String getPath() {
        return this.path;
    }
}
