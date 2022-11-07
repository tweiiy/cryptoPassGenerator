package com.tweiiy.cryptopassgenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class Controller {

    @FXML
    private TextArea Comment;

    @FXML
    private TextArea Length;

    @FXML
    private CheckBox isLetters;

    @FXML
    private CheckBox isNumbers;

    @FXML
    private CheckBox isSymbols;

    @FXML
    private Label password;

    @FXML
    private TableView<SavedPassword> table;
    @FXML
    private TableColumn<SavedPassword, String> commentColumn;
    @FXML
    private TableColumn<SavedPassword, String> passwordsColumn;

    @FXML
    private CheckBox isUpper;
    public Numbers numbers = new Numbers(); //объект цифр
    public Letters letters = new Letters(); //объект букв
    public Symbols symbols = new Symbols(); //объект символов
    File file = new File("passwords.txt");
    String secretKey = "3koCoyqI5L61iXFq";
    @FXML
    private void initialize() throws IOException {
        SavedPasswords.fillPasswords(file, secretKey);
        SavedPasswords.showPasswords();
        passwordsColumn.setCellValueFactory(new PropertyValueFactory<SavedPassword, String>("password"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<SavedPassword, String>("comment"));
        table.setItems(SavedPasswords.getPasswords());
    }
    @FXML
    void ChangeLetters(ActionEvent event) {
        letters.isActive = isLetters.isSelected();
    } // смена активности букв
    @FXML
    void ChangeNumbers(ActionEvent event) {
        numbers.isActive = isNumbers.isSelected();
    } // смена активности цифр
    @FXML
    void ChangeSymbols(ActionEvent event) {
        symbols.isActive = isSymbols.isSelected();
    } // смена активности символов
    @FXML
    void CopyGeneratorOnClick(ActionEvent event) {
        StringSelection selection = new StringSelection(password.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    } // копирование в буфер обмена
    @FXML
    void SaveGeneratorOnClick(ActionEvent event) throws IOException {
        if(!file.exists())
            file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        try{
            writer.write(AesCipher.encrypt(secretKey, password.getText())+' '+Comment.getText()+"\n");
            SavedPasswords.addPass(file, new SavedPassword(password.getText(),Comment.getText()));
        } catch (IOException e){
            System.out.println(e);
        } finally {
            writer.close();
        }
        SavedPasswords.showPasswords();
        table.setItems(SavedPasswords.getPasswords());
    }
    @FXML
    void generateOnClick(ActionEvent event) {
        String strLength = Length.getText(); // получение длины пароля
        int length; // объявление числовой переменной для пароля
        if (strLength.length()!=0) // проверка, есть ли в поле символы
            length = Integer.parseInt(strLength); // конвертация типов, если строка не пуста
        else
            length=8; // установление стандартной длины, если строка пуста
        ArrayList allSymbols = new ArrayList(0); // массив всех символов, на основе которых будет генерироваться пароль
        char randomSymbol;
        int randomInt;
        String shortPass="";
        if (numbers.isActive)
            allSymbols.addAll(numbers.chars); // добавление в массив цифр
        if (letters.isActive)
            allSymbols.addAll(letters.chars); // добавление в массив букв
        if (symbols.isActive)
            allSymbols.addAll(symbols.chars); // добавление в массив символов
        if(allSymbols.size()>0){
            for (int index = 0; index < length; index++){
                randomInt=((int)(Math.random()*67))%allSymbols.size();//генерация случайного индекса
                randomSymbol= (char) allSymbols.get(randomInt);//выбор символа на основе случайного индекса
                if(isUpper.isSelected()&&letters.chars.contains(randomSymbol)&&((int)((Math.random()*10)%2)>0)){
                    randomSymbol=Character.toUpperCase(randomSymbol);
                }
                shortPass+=randomSymbol;
            }
            password.setText(shortPass);
        }else
            password.setText("password");
    } // генерация пароля
    @FXML
    void SavedCopy(ActionEvent event) {
        StringSelection selection = new StringSelection(table.getSelectionModel().getSelectedItem().getPassword());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
    @FXML
    void Delete(ActionEvent event) throws IOException {
        SavedPassword selectedItem = table.getSelectionModel().getSelectedItem();
        table.getItems().remove(selectedItem);
        SavedPasswords.removePass(file, selectedItem);

        String removeString = AesCipher.encrypt(secretKey ,selectedItem.getPassword())+' '+selectedItem.getComment();

        List<String> lines= FileUtils.readLines(file, StandardCharsets.UTF_8);
        List<String> notEmptyLines = new ArrayList<>();
        for(String line: lines){
            System.out.println(line+"\n"+removeString);
            if (!line.isEmpty()&&!line.equals(removeString)){
                notEmptyLines.add(line);
            }
        }
        file.delete();
        file.createNewFile();
        FileUtils.writeLines(file,notEmptyLines);
    }
}
