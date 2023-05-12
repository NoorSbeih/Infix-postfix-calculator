package com.example.project2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HelloApplication extends Application {
    CursorStack stack = new CursorStack();
    int l = stack.createStack();
    CursorStack stack2 = new CursorStack();
    int p = stack2.createStack();
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();

        root.setAlignment(Pos.TOP_CENTER);
        root.setId("pane");
        Scene scene = new Scene(root, 520, 600);


        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        scene.setFill(Color.TRANSPARENT);


        TextField result = new TextField("Choose a file to load");
        result.setPrefWidth(300);
        result.setEditable(false);


        TextArea eq = new TextArea();
        eq.setMaxWidth(500);
        eq.setPrefHeight(300);
        eq.setTranslateY(10);
        eq.setEditable(false);

        Label title = new Label("Infix to postfix calculator :)");
        title.setTranslateY(10);
        title.setFont(Font.font("Times new roman", FontWeight.BOLD, FontPosture.REGULAR, 18));

        Button exit = new Button("X");
        exit.setId("b2");
        exit.setTranslateX(220);
        exit.setOnAction(e -> Platform.exit());


        Button minimize = new Button("_");
        minimize.setId("b2");
        minimize.setTranslateX(220);
        minimize.setOnAction(e -> stage.setIconified(true));


        HBox panel = new HBox(title, minimize, exit);


        Button loadButt = new Button("Load");
        loadButt.setPrefWidth(100);
        loadButt.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        loadButt.setTextFill(Color.GREEN);


        Button back = new Button("←");
        back.setPrefWidth(50);
        back.setId("b1");
        back.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        back.setTextFill(Color.RED);
        back.setDisable(true);


        Button front = new Button("→");
        front.setId("b1");
        front.setPrefWidth(50);
        front.setTextFill(Color.RED);
        front.setDisable(true);


        Label equations = new Label("Equations:");
        equations.setTranslateY(10);
        equations.setFont(Font.font("Times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));


        Label files = new Label("Files:");
        files.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        files.setTranslateY(10);


        TextFlow fileBox = new TextFlow();
        fileBox.setPrefWidth(500);
        fileBox.setPrefHeight(150);
        fileBox.setTranslateY(10);
        fileBox.setId("textFlow");


        loadButt.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/Data"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                stack.push(selectedFile, l);
                loadFile((File) stack.getPeek(l), eq, fileBox, result);
                back.setDisable(stack.isEmpty(l));
                stack2.empty(p);
                front.setDisable(true);
            }


        });

        back.setOnAction(e -> {
            if (!stack.isEmpty(l)) {
                stack2.push(stack.pop(l).getData(), p);
                if (!stack.isEmpty(l))
                    loadFile((File) stack.getPeek(l), eq, fileBox, result);
                else {
                    result.setText("Choose a file to load");
                    eq.setText("");
                    fileBox.getChildren().clear();
                }
            }
            if (stack.isEmpty(l))
                back.setDisable(true);
            if (!stack2.isEmpty(p))
                front.setDisable(false);


        });

        front.setOnAction(e -> {
            if (!stack2.isEmpty(p)) {
                loadFile((File) stack2.getPeek(p), eq, fileBox, result);
                stack.push(stack2.pop(l).getData(), p);
                if (stack2.isEmpty(p))
                    front.setDisable(true);
            } else front.setDisable(true);

            if (!stack.isEmpty(l))
                back.setDisable(false);


        });


        HBox hbox = new HBox(back, front, result, loadButt);
        VBox vbox = new VBox(panel, hbox, equations, eq, files, fileBox);
        hbox.setTranslateY(5);

        root.getChildren().add(vbox);


        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        stage.getIcons().add(new Image("C:\\Users\\sbeih\\IntelliJ workspace\\project2\\src\\main\\resources\\com\\example\\project2\\icon.png"));
        stage.setTitle("Infix to Postfix calculator");
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    public void loadFile(File selectedFile, TextArea eq, TextFlow file, TextField result) { //method to load file
        try {
            if(!getFileExtension(selectedFile.getName()).equals("242")) {
                result.setText("Please select a .242 file");
                eq.setText("");
                file.getChildren().clear();
                return;
            }

                Scanner read = new Scanner(selectedFile);
                result.setText(selectedFile.getCanonicalPath());
                eq.setText("");
                file.getChildren().clear();


                StringBuilder s = new StringBuilder();
                while (read.hasNextLine())
                    s.append(read.nextLine()).append("\n");
                if (!(s.toString().replaceAll("\\s","").startsWith("<242>") && s.toString().replaceAll("\\s","").endsWith("</242>"))) {
                    result.setText("Invalid file!");
                } else if (isBalanced(s.toString())) {
                    Pattern pattern = Pattern.compile("<equation>(.*?)</equation>", Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(s.toString());
                    Pattern pattern1 = Pattern.compile("<equations>(.*?)</equations>", Pattern.DOTALL);
                    Matcher matcher1 = pattern1.matcher(s.toString());
                    Pattern pattern2 = Pattern.compile("<file>(.*?)</file>", Pattern.DOTALL);
                    Matcher matcher2 = pattern2.matcher(s.toString());
                    Pattern pattern4 = Pattern.compile("<files>(.*)</files>", Pattern.DOTALL);
                    Matcher matcher4 = pattern4.matcher(s.toString());
                    if (matcher1.find()) {
                        while (matcher.find()) {
                            Equation x = new Equation(matcher.group(1));
                            if (x.getPostfix().equals("Invalid equation") || x.getPostfix().equals("Imbalanced equation"))
                                eq.appendText(x.getData() + "=>" + x.getPostfix() + "\n\n");
                            else
                                eq.appendText(x.getData() + "=>" + x.getPostfix() + "=>" + x.getResult() + "\n\n");
                        }
                    }

                    if (matcher4.find()) {
                        while (matcher2.find()) {
                            Hyperlink link = new Hyperlink(matcher2.group(1).replaceAll("c:\\\\data\\\\", "").replaceAll("\\s", ""));
                            file.getChildren().add(link);
                            file.getChildren().add(new Text(System.lineSeparator()));
                            link.setOnAction(e -> {
                                StringBuilder x = new StringBuilder("C:\\data\\");
                                x.append(link.getText());
                                loadFile(new File(x.toString()), eq, file, result);
                                stack.push(new File(x.toString()), l);
                            });

                        }
                    }
                    if (eq.getText().isEmpty())
                        eq.setText("No equations found");
                    if (file.getChildren().isEmpty()) {
                        Label x = new Label("No files found");
                        x.setId("text");
                        x.setTranslateX(10);
                        x.setTranslateY(10);
                        file.getChildren().add(x);

                    }

                } else result.setText("Invalid file!");



        } catch (Exception ex) {
            result.setText("Invalid selection!");
            file.getChildren().clear();
            eq.setText("");

        }
    }
    public boolean isBalanced(String data){  //function to check if file is balanced (start and end tags)
        CursorStack balance =new CursorStack();
        int l=balance.createStack();
        Pattern files = Pattern.compile("<(.?)files>",Pattern.DOTALL);
        Matcher filesMatch = files.matcher(data);
        Pattern equations = Pattern.compile("<(.?)equations>",Pattern.DOTALL);
        Matcher equationsMatch = equations.matcher(data);
        Pattern file = Pattern.compile("<(.?)file>",Pattern.DOTALL);
        Matcher fileMatch = file.matcher(data);
        Pattern equation = Pattern.compile("<(.?)equation>",Pattern.DOTALL);
        Matcher equationMatch = equation.matcher(data);
         while(equationsMatch.find()){
             if(equationsMatch.group().equals("<equations>")){
                 balance.push(equationsMatch.group(),l);
                 while(equationMatch.find()){
                     if(equationMatch.group().equals("<equation>"))
                         balance.push(equationMatch.group(),l);
                     else{
                         if(!balance.getPeek(l).equals("<equation>"))
                             return false;
                         else balance.pop(l)    ;
                     }
                 }
             }
             else{
                 if(!balance.getPeek(l).equals("<equations>"))
                     return false;
                 else balance.pop(l);
             }
         }
        while(filesMatch.find()){
            if(filesMatch.group().equals("<files>")){
                balance.push(filesMatch.group(),l);
                while(fileMatch.find()){
                    if(fileMatch.group().equals("<file>"))
                        balance.push(fileMatch.group(),l);
                    else{
                        if(!balance.getPeek(l).equals("<file>"))
                            return false;
                        else balance.pop(l)    ;
                    }
                }
            }
            else{
                if(!balance.getPeek(l).equals("<files>"))
                    return false;
                else balance.pop(l);
            }
        }
        if(fileMatch.find() || equationMatch.find())
            return false;



        return balance.isEmpty(l);
    }
    public static String getFileExtension(String name) { //method to check file extension
        String fileName = new File(name).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static void main(String[] args) {
        launch();
    }
}