package year2019.march;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public abstract class BaseCharacter {

    private static final int MAX_ATTRIBUTE_POINTS = 35;

    private String name;

    IntegerProperty hp = new SimpleIntegerProperty(150);

    private int strength = 1;
    private int vitality = 1;
    private int intellect = 1;

    private int luck = 5;

    private int attributePoints = MAX_ATTRIBUTE_POINTS;

    ObjectProperty<Element> armorElement = new SimpleObjectProperty<>(Element.NEUTRAL);
    ObjectProperty<Element> weaponElement = new SimpleObjectProperty<>(Element.NEUTRAL);

    private int critCount = 2;
    private int invulCount = 1;

    private boolean isCritical = false;
    private boolean isInvulnerable = false;

    private List<Move> opponentMoves = new ArrayList<>();

    public final String getName() {
        return name;
    }

    protected final void setName(String name) {
        this.name = name;
    }

    public final int getHp() {
        return hp.getValue();
    }

    final void takeDamage(int value) {
        hp.setValue(hp.getValue() - value);
    }

    boolean isDead() {
        return getHp() <= 0;
    }

    public final boolean isCritical() {
        return isCritical;
    }

    protected final void setNextMoveAsCritical() {
        if (critCount == 0)
            return;

        isCritical = true;

        // if lots of luck and 50% chance works, then this critical is free
        if (luck > 20 && Math.random() < 0.5) {
            critCount++;
        }

        critCount--;
    }

    public final int getStrength() {
        return strength;
    }

    public final int getVitality() {
        return vitality;
    }

    public final int getIntellect() {
        return intellect;
    }

    public final int getLuck() {
        return luck;
    }

    public final int getDamageFrom(MoveType type) {
        int dmg = calcDamage(type);

        // deal at least 1 damage
        return Math.max(dmg, 1);
    }

    private int calcDamage(MoveType type) {
        switch (type) {
            case ATTACK:
                return strength + CombatMath.getRandomLuckDamage(luck);
            case SKILL:
                return intellect + CombatMath.getRandomLuckDamage(luck);
            case BLOCK:
                return vitality + CombatMath.getRandomLuckDamage(luck);
            default:
                throw new RuntimeException("Can't happen! " + type);
        }
    }

    protected final void addStrength(int value) {
        checkValue(value);

        strength += value;
        attributePoints -= value;
    }

    protected final void addVitality(int value) {
        checkValue(value);

        vitality += value;
        attributePoints -= value;
    }

    protected final void addIntellect(int value) {
        checkValue(value);

        intellect += value;
        attributePoints -= value;
    }

    protected final void addLuck(int value) {
        checkValue(value);

        luck += value;
        attributePoints -= value;
    }

    protected final void setArmorElement(Element armorElement) {
        this.armorElement.setValue(armorElement);
    }

    final Element getArmorElement() {
        return armorElement.getValue();
    }

    protected final void setWeaponElement(Element weaponElement) {
        this.weaponElement.setValue(weaponElement);
    }

    final Element getWeaponElement() {
        return weaponElement.getValue();
    }

    final boolean isInvulnerable() {
        return isInvulnerable;
    }

    protected void setNextMoveInvulnerable() {
        if (invulCount == 0)
            return;

        invulCount--;
        isInvulnerable = true;
    }

    protected void resetAttributePoints() {
        strength = 1;
        vitality = 1;
        intellect = 1;

        luck = 5;

        attributePoints = MAX_ATTRIBUTE_POINTS;
    }

    final void addOpponentMove(Move move) {
        opponentMoves.add(move);
    }

    protected List<Move> getOpponentMoves() {
        return opponentMoves;
    }

    final void onMoveFinished() {
        isCritical = false;
        isInvulnerable = false;
    }

    protected abstract Move makeMove(BaseCharacter other);

    private void checkValue(int value) {
        if (value < 0)
            throw new RuntimeException("Value cannot be negative!");

        if (value > attributePoints)
            throw new RuntimeException("You cannot use more than " + MAX_ATTRIBUTE_POINTS + " attribute points!");
    }
}
