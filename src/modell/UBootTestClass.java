//package modell;
//
//import modell.Territorium.FeldEigenschaft;
//
//public abstract class UBootTestClass extends UBoot {
//
//	public static void main(String[] agrs) {
//		UBoot uboot = new UBoot();
////		uboot.setBeleuchtung(true);
////		uboot.changeSize(11, 11);
////		uboot.setzeUBootAufsFeld(6, 5);
////		uboot.lichtAusBzwAn(false);
////		uboot.setzeObjektAufsFeld(0, 3, FeldEigenschaft.ZielFeld);
////		uboot.setzeObjektAufsFeld(0, 2, FeldEigenschaft.Batterie);
////		uboot.setzeObjektAufsFeld(3, 2, FeldEigenschaft.Felsen);
////		uboot.setzeObjektAufsFeld(0, 0, FeldEigenschaft.Batterie);
////		uboot.setTerritoriumSpecialFinishNummer(9000);
////		uboot.changeSize(4, 4);
////		print(uboot);
//
//		boolean b = false; 
//		while (b) {
//			String s = IO.readString();
//			switch (s) {
//			case "v":
//				uboot.vorFahren();
//				break;
//			case "b":
//				uboot.rueckFahren();
//				break;
//			case "l":
//				uboot.linksDrehen();
//				break;
//			case "r":
//				uboot.rechtsDrehen();
//				break;
//			case "s":
//				uboot.sonarAktivieren();
//				break;
//			case "n":
//				uboot.nihmBatterie();
//				break;
//			case "v?":
//				if(uboot.felsenDaOderSpielfeldEndeAbfrageVor()){
//					System.out.println("Felsen Voraus");
//				}else{
//					System.out.println("Freie Fahrt");
//				}
//				break;
//			case "n?":
//				if(uboot.batterieDa()){
//					System.out.println("Strom unter uns");
//				}else{
//					System.out.println("Keine Batterie da");
//				}
//				break;
//			default:
//				b = false;
//				break;
//			}
//			print(uboot.getTerritorium());
//		}
//	}
//
//	private static void print(Territorium t) {
//		// String s = " 0 1 2 3 4 5 6 7 8 9 10" + "\n";
//		String s = "";
//		for (int i = 0; i < t.getFeld().length; i++) {
//			// s = s + i;
//			for (int j = 0; j < t.getFeld()[i].length; j++) {
//				if (t.istFeldDunkelNachFeldGroe�e[i][j]) {
//					s = s + " @";
//				} else if (t.feldReiheBug == i & t.feldSpalteBug == j) {
//					s = s + " B";
//				} else if (t.feldReiheHeck == i & t.feldSpalteHeck == j) {
//					s = s + " H";
//				} else if (t.getFeld()[i][j] == FeldEigenschaft.Batterie) {
//					s = s + " A";
//				} else if (t.getFeld()[i][j] == FeldEigenschaft.Felsen) {
//					s = s + " F";
//				} else if (t.getFeld()[i][j] == FeldEigenschaft.LeuchtFelsen) {
//					s = s + " L";
//				} else if (t.getFeld()[i][j] == FeldEigenschaft.ZielFeld) {
//					s = s + " Z";
//				} else {
//					s = s + "  ";
//				}
//			}
//			s = s + "\n";
//		}
//		System.out.println(s);
//	}
//}
