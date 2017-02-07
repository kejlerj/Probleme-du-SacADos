import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SacADos {
	private float poids;
	private float valeur;
	private int poidsMax;
	private String chemin;
	private ArrayList<Objet> listeObjets;

	public SacADos(){
		this.poids = 0;
		this.valeur = 0;
		this.poidsMax = 0;
		this.chemin = "";
		this.listeObjets = new ArrayList<Objet>();
	}

	public SacADos(String chemin, int poidsMax){
		this.poids = 0;
		this.valeur = 0;
		this.poidsMax = poidsMax;
		this.chemin = chemin;
		this.listeObjets = new ArrayList<Objet>();
		this.remplirListeObjet();
	}

	private void remplirListeObjet(){
		//importation des objets
		try {
			Scanner sc = new Scanner(new File(this.chemin));
			String ligne = "";
			while (sc.hasNext()){
				ligne = sc.nextLine(); //on lit ligne par ligne.
				String tab[] = ligne.split(";"); //divise par un separateur ;
				Objet o = new Objet(tab[0], Float.valueOf(tab[1]), Float.valueOf(tab[2])); //on recupereles champs du fichier pour creer les objets
				this.listeObjets.add(o); //ajouter un objet dans la liste.
			}
			sc.close();
		}
		catch(FileNotFoundException e){
			System.out.println("Erreur lors de l'ouverture du fichier !");
			return;
		}
		return;
	}

	private void ajouterDansLeSac(int index){
		this.poids += this.listeObjets.get(index).getPoids();
		this.valeur += this.listeObjets.get(index).getValeur();
		this.listeObjets.get(index).mettreDansLeSac();
	}

	public void viderSac(){
		this.poids = 0;
		this.valeur = 0;
		for (int i = 0; i < this.listeObjets.size(); ++i){
			this.listeObjets.get(i).sortirDuSac();
		}
	}

	private boolean sacPlein(){
		for(int i = 0; i < this.listeObjets.size(); ++i){
			if(this.listeObjets.get(i).getDansLeSac())
				return true;
		}
		return false;
	}

	public void resoudre(String methode){

		if(methode.compareTo("gloutonne") == 0){
			this.methode_gloutonne();
			System.out.println("Méthode gloutonne : \n" + this.toString() + "\n\n");
		}	
		else if(methode.compareTo("dynamique") == 0){
			this.methode_dynamique();
			System.out.println("Méthode dynamique : \n" + this.toString() + "\n\n");
		}
		else if(methode.compareTo("PSE") == 0){
			this.methode_PSE();
			System.out.println("Méthode PSE : \n" + this.toString() + "\n\n");
		}
	}
	
	private void methode_gloutonne(){
		this.quickSort(0, this.listeObjets.size() - 1);
		for(int i = 0; i < this.listeObjets.size(); ++i){
			if(this.poids + this.listeObjets.get(i).getPoids() <= this.poidsMax)
				this.ajouterDansLeSac(i);
		}
	}

	private void quickSort(int debut, int fin){
		if(debut < fin){
			int pivot = debut;
			int gauche = debut;
			permuter(pivot, fin);
			for(int i = debut; i < fin; ++i){
				if(this.listeObjets.get(i).getRatio() > this.listeObjets.get(fin).getRatio()){
					permuter(i, gauche);
					gauche++;
				}
			}
			permuter(fin, gauche);
			pivot = gauche;
			quickSort(debut, pivot - 1);
			quickSort(debut + 1, fin);
		}
	}	

	private void permuter(int a, int b){
		Objet tmp = this.listeObjets.get(a);
		this.listeObjets.set(a, this.listeObjets.get(b));
		this.listeObjets.set(b, tmp);
	}

	private void methode_dynamique(){
		if(this.poidsMax == 0){
			return;
		}

		float[][] tab = new float[this.listeObjets.size()][this.poidsMax + 1];
		int i = 0, j = 0;

		//Remplissage de la matrice tab ou chauqe case  représente le bénéfice maximum possible pour les i premiers objets avec un poids j
		for(j = 0; j <= this.poidsMax; ++j){
			if(this.listeObjets.get(0).getPoids() > j)
				tab[0][j] = 0;
			else
				tab[0][j] = this.listeObjets.get(0).getValeur();
			for(i = 1; i < this.listeObjets.size(); ++i){
				if(this.listeObjets.get(i).getPoids() > j)
					tab[i][j] = tab[i - 1][j];
				else
					tab[i][j] = lePlusGrand( tab[i - 1][j], tab[i - 1][j - (int)this.listeObjets.get(i).getPoids()] + this.listeObjets.get(i).getValeur());
			}
		}
		//Parcours de la matrice pour retrouver les objets a mettre dans le sac
		i = this.listeObjets.size() - 1;
		j = this.poidsMax;
		while(tab[i][j] == tab[i][j - 1])
			j--;
		while(j > 0 && i >= 0){
			while(i > 0 && tab[i][j] == tab[i - 1][j])
				i--;
			j = j - (int)this.listeObjets.get(i).getPoids();
			if( j >= 0)
				this.ajouterDansLeSac(i);
			i--;
		}
	}

	private float lePlusGrand(float a, float b){
		if (a > b)
			return a;
		else return b;
	}

	private void methode_PSE(){

		//On construit l'arbre
		Feuille arbre = new Feuille(new int[1], 0, this.listeObjets.size(), false);
		// On recupere la valeur du sac qui sors de l'algo Heuristique pour l'attribuer a borneInf
		this.methode_gloutonne();
		arbre.setBorneInf(this.valeur);
		this.viderSac();

		//Parcours de l'arbre et résolution
		remplir(arbre);
		explorer(arbre);
	}

	private void remplir(Feuille a){
		this.setPoidsFeuille(a);
		this.setValeurFeuille(a);
		this.setBorneSup(a);
		this.setBorneInf(a);

		if(a.getFeuilleG() != null)
			this.remplir(a.getFeuilleG());
		if(a.getFeuilleD() != null)
			this.remplir(a.getFeuilleD());
	}

	private void explorer(Feuille a){

		// Si la meilleur solution a été trouvé on rempli le sac
		
		if(a.getValeur() == a.getBorneInf()){
			for(int i = 1; i < a.getTab().length; ++i)
				this.ajouterDansLeSac(a.getTab()[i] - 1);
			return;
		}

		if(!this.sacPlein()){
			/* On n'explore les branches que si BorneInf < BorneSup
		Sinon on coupe l'arbre*/
			if(a.getFeuilleG().getBorneInf() <= a.getFeuilleG().getBorneSup() && a.getFeuilleG().getPoids() <= this.poidsMax){
				if(a.getFeuilleG() != null){
					explorer(a.getFeuilleG());
				}
			}
			if(a.getFeuilleD().getBorneInf() <= a.getFeuilleD().getBorneSup() && a.getFeuilleD().getPoids() <= this.poidsMax){
				if(a.getFeuilleD() != null){
					explorer(a.getFeuilleD());
				}
			}
		}
	}


	private  void setPoidsFeuille(Feuille a){
		float p = 0;
		for(int i = 1; i < a.getTab().length; ++i){
			p += this.listeObjets.get(a.getTab()[i] - 1).getPoids();
		}
		a.setPoids(p);
	}

	private  void setValeurFeuille(Feuille a){
		float v = 0;
		for(int i = 1; i < a.getTab().length; ++i){
			v += this.listeObjets.get(a.getTab()[i] - 1).getValeur();
		}
		a.setValeur(v);
	}

	private void setBorneSup(Feuille a){
		/*Elle vaut la valeur de tab + la valeur de tous les 
			autres objets qu'on peut encore mettre dedans*/
		float b = 0;

		for(int i = 1; i < a.getTab().length; ++i){
			b += this.listeObjets.get(a.getTab()[i] - 1).getValeur();	
		}
		for (int i = a.getPos(); i < this.listeObjets.size(); ++i){
			b += this.listeObjets.get(i).getValeur();
		}

		a.setBorneSup(b);
		//System.out.println("BorneSup de " + a.afficherTab() + " et pos " + a.getPos() + " : " + a.getBorneSup());
	}

	private void setBorneInf(Feuille a){
		if(a.getPoids() <= this.poidsMax){
			if(a.getBorneInf() < a.getValeur())
				a.setBorneInf(a.getValeur());
		}
	}

	public String toString(){
		String affichage = "";
		for (int i = 0; i < this.listeObjets.size(); ++i){
			if(this.listeObjets.get(i).getDansLeSac())
				affichage = affichage + "- " + this.listeObjets.get(i).getNom() + this.listeObjets.get(i).getValeur() + " " + this.listeObjets.get(i).getPoids() + "\n";
		}
		affichage = "Le sac contient : \n" + affichage + "\n\nPoids total : " + this.poids + "/" + this.poidsMax + "\nValeur total : " + this.valeur;
		return affichage;
	}
}
