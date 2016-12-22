package gui;

import data.Attribute;
import data.ToFCharacter;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class AttributePanel {
  private final MainFrame mainFrame;
  private Map<Attribute, TextField> baseValues;
  private Map<Attribute, Label> baseMods;
  private Map<Attribute, Label> modifiedValues;
  private Map<Attribute, Label> modifiedMods;
  private final DecimalFormat fmt;
  private final GridPane pane;

  public AttributePanel(MainFrame mainFrame, ToFCharacter character){
    this.mainFrame = mainFrame;
    baseValues = new TreeMap<>();
    baseMods = new TreeMap<>();
    modifiedValues = new TreeMap<>();
    modifiedMods = new TreeMap<>();
    fmt = new DecimalFormat("+#0;-#");

    for(Attribute attribute: Attribute.values()) {
      TextField txt = new TextField();
      txt.setPrefColumnCount(2);
      int baseValue = character.getBaseAttribute(attribute);
      txt.setText(String.valueOf(baseValue));

      txt.textProperty().addListener((observable, oldValue, newValue) -> {
          if (!newValue.matches("\\d*")) {
            Platform.runLater(() -> {
              txt.setText(newValue.replaceAll("[^\\d*]", ""));
              txt.positionCaret(txt.getLength());
            });
          } else {
            try {
              int value = Integer.parseInt(newValue);
              character.setAttribute(attribute, value);
              mainFrame.update(character);
            } catch (NumberFormatException e) {
            }
          }
        });
      baseValues.put(attribute, txt);

      int mod = Attribute.getModifier(baseValue);
      Label lblBaseMod = new Label(fmt.format(mod));
      baseMods.put(attribute, lblBaseMod);

      int moddedValue = character.getModifiedAttribute(attribute);
      Label lblModValues = new Label(String.valueOf(moddedValue));
      modifiedValues.put(attribute, lblModValues);

      int moddedMod = Attribute.getModifier(moddedValue);
      Label lblModMod = new Label(fmt.format(moddedMod));
      modifiedMods.put(attribute, lblModMod);

    }

    pane = MainFrame.getGridPane();
    pane.add(new Label("Base Attributes"), 0, 0, 3, 1);
    pane.add(new Label("Current Attributes"), 3, 0, 3, 1);
    for (Attribute attribute: Attribute.values()) {
      pane.addRow(attribute.index(), new Label(attribute.getAbbrevation()), baseValues.get(attribute), baseMods.get(attribute), modifiedValues.get(attribute), modifiedMods.get(attribute));
    }
  }

  public void update(ToFCharacter character) {
    for (Attribute attribute: Attribute.values()) {
      int value = character.getBaseAttribute(attribute);
      int moddedValue = character.getModifiedAttribute(attribute);
      baseValues.get(attribute).setText(String.valueOf(value));
      baseMods.get(attribute).setText(fmt.format(Attribute.getModifier(value)));
      modifiedValues.get(attribute).setText(String.valueOf(moddedValue));
      baseMods.get(attribute).setText(fmt.format(Attribute.getModifier(moddedValue)));
    }
  }

  public Pane getPanel() {
    return pane;
  }


  /**
   GridPane grid = getGridPane();

   int y = 1;
   for (Attribute a : Attribute.values()) {
   TextField attributeField = new TextField();
   Label modifier = new Label("");
   Label modifiedValue = new Label("");
   Label modifiedModifier = new Label("");


   DecimalFormat fmt = new DecimalFormat("+#0;-#");


   attributeField.setText(String.valueOf(character.getBaseAttribute(a)));
   attributeField.setPrefWidth(35);
   grid.add(new Label(a.getAbbrevation()), 0, y);
   grid.add(attributeField, 1, y);
   grid.add(modifier, 2, y);
   grid.add(modifiedValue, 3, y);
   grid.add(modifiedModifier, 4, y);
   y++;
   }

   return grid; **/
}