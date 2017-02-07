
public class Appli {

	public static void main(String[] args) {
		//chemin du fichier items.txt --> "ressource\\items.txt"

		if(args.length != 3){
			System.out.println("Erreur de saisie");
			return;
		}
		String chemin = args[0];
		int pds = Integer.parseInt(args[1]);
		String methode = args[2];

		SacADos sac = new SacADos(chemin, pds);
		sac.resoudre(methode);
	}
}
