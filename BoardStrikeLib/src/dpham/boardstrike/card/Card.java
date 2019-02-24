package dpham.boardstrike.card;

import java.util.LinkedHashMap;

import dpham.boardstrike.piece.PieceType;

/**
 * Class used to simulate a card in the game.
 * @author Daniel Pham
 *
 */
public class Card {
	
	/** The name of the card */
	private String name;
	/** The description of the card */
	private String desc;
	
	public static LinkedHashMap<String, Card> cards = new LinkedHashMap<String, Card>();
	
	static {
		cards.put("FOOTMAN_CONTRACT", new SummonCard("Footman Contract", "Summon a Footman", PieceType.FOOTMAN));
		cards.put("ACOLYTE_CONTRACT", new SummonCard("Acolyte Contract", "Summon an Acolyte", PieceType.ACOLYTE));
		
		LinkedHashMap<PieceType, PieceType> rangerSeal = new LinkedHashMap<PieceType, PieceType>();
		rangerSeal.put(PieceType.FOOTMAN, PieceType.LANCER);
		rangerSeal.put(PieceType.LANCER, PieceType.SNIPER);
		rangerSeal.put(PieceType.SCOUT, PieceType.ARCHER);
		
		
		LinkedHashMap<PieceType, PieceType> bloodSeal = new LinkedHashMap<PieceType, PieceType>();
		bloodSeal.put(PieceType.FOOTMAN, PieceType.MERCENARY);
		bloodSeal.put(PieceType.MERCENARY, PieceType.GLADIATOR);
		bloodSeal.put(PieceType.SCOUT, PieceType.ASSASSIN);
		
		LinkedHashMap<PieceType, PieceType> imperialSeal = new LinkedHashMap<PieceType, PieceType>();
		imperialSeal.put(PieceType.FOOTMAN, PieceType.SCOUT);
		imperialSeal.put(PieceType.LANCER, PieceType.HALBERDIER);
		imperialSeal.put(PieceType.MERCENARY, PieceType.DEFENDER);
		
		LinkedHashMap<PieceType, PieceType> holySeal = new LinkedHashMap<PieceType, PieceType>();
		holySeal.put(PieceType.ACOLYTE, PieceType.PRIEST);
		holySeal.put(PieceType.PRIEST, PieceType.CLERIC);
		
		LinkedHashMap<PieceType, PieceType> arcaneSeal = new LinkedHashMap<PieceType, PieceType>();
		arcaneSeal.put(PieceType.ACOLYTE, PieceType.MAGE);
		arcaneSeal.put(PieceType.MAGE, PieceType.SORCERER);
		
		LinkedHashMap<PieceType, PieceType> heroSeal = new LinkedHashMap<PieceType, PieceType>();
		heroSeal.put(PieceType.DEFENDER, PieceType.HERO);
		
		LinkedHashMap<PieceType, PieceType> dragoonSeal = new LinkedHashMap<PieceType, PieceType>();
		dragoonSeal.put(PieceType.HALBERDIER, PieceType.DRAGOON);
		
		cards.put("RANGER_SEAL", new PromoteCard("Ranger Seal", "", rangerSeal));
		cards.put("BLOOD_SEAL", new PromoteCard("Blood Seal", "", bloodSeal));
		cards.put("IMPERIAL_SEAL", new PromoteCard("Imperial Seal", "", imperialSeal));
		cards.put("HOLY_SEAL", new PromoteCard("Holy Seal", "", holySeal));
		cards.put("ARCANE_SEAL", new PromoteCard("Arcane Seal", "", arcaneSeal));
		cards.put("HERO_SEAL", new PromoteCard("Hero's Seal", "", heroSeal));
		cards.put("DRAGOON_SEAL", new PromoteCard("Dragoon's Seal", "", dragoonSeal));
	}
	
	/**
	 * Constructor to create a card
	 * @param name		The name of the card
	 * @param desc		The description of the card
	 */
	public Card(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	/**
	 * Gets the name of the card
	 * @return		The name of the card
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the description of the card
	 * @return		The description of the card
	 */
	public String getDesc() {
		return desc;
	}

}
