package com.example.tecnicas_reto11;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class MainController implements Initializable {


    Alert a = new Alert(Alert.AlertType.NONE);
    List<Movimiento> movimientos = new ArrayList<>();

    @FXML
    public LineChart lineChart;

    @FXML
    public Button menorButton;
    
    @FXML
    public Button mayorButton;

    @FXML
    public Button desviacionButton;

    @FXML
    public Button promedioButton;
    
    @FXML
    public Button errButton;
    
    @FXML
    private TableView dataTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        BufferedReader bufferLectura = null;
        try {
            bufferLectura = new BufferedReader(new FileReader("docs/BTC-USD.csv"));
            String linea = bufferLectura.readLine();
            linea = bufferLectura.readLine();
            while (linea != null) {
                String[] campos = linea.split(",");
                movimientos.add(new Movimiento(campos[0],Double.parseDouble(campos[1]),Double.parseDouble(campos[2]),Double.parseDouble(campos[3]),Double.parseDouble(campos[4]),Double.parseDouble(campos[5]),Double.parseDouble(campos[6])));
                linea = bufferLectura.readLine();
            }
        }
        catch (IOException e) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("El error es: "+e.getMessage());
            a.show();

        }
        finally {
            if (bufferLectura != null) {
                try {
                    bufferLectura.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        TableColumn date = new TableColumn("Date");
        TableColumn open = new TableColumn("Open");
        TableColumn high = new TableColumn("High");
        TableColumn low = new TableColumn("Low");
        TableColumn close = new TableColumn("Close");
        TableColumn adjClose = new TableColumn("adjClose");
        TableColumn volume = new TableColumn("Volume");
        dataTable.getColumns().addAll(date,open,high,low,close,adjClose,volume);

        ObservableList<Movimiento> datosMovimientos = FXCollections.observableArrayList(movimientos);

        date.setCellValueFactory(new PropertyValueFactory<Movimiento,String>("date"));
        open.setCellValueFactory(new PropertyValueFactory<Movimiento,Double>("open"));
        high.setCellValueFactory(new PropertyValueFactory<Movimiento,Double>("high"));
        low.setCellValueFactory(new PropertyValueFactory<Movimiento,Double>("low"));
        close.setCellValueFactory(new PropertyValueFactory<Movimiento,Double>("close"));
        adjClose.setCellValueFactory(new PropertyValueFactory<Movimiento,Double>("adjClose"));
        volume.setCellValueFactory(new PropertyValueFactory<Movimiento,Double>("volume"));

        dataTable.setItems(datosMovimientos);

        // Inicio de line chart
        lineChart.setTitle("Movimientos del Bitcoin");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        lineChart.getXAxis().setLabel("Dia (Cada 15) ");
        lineChart.getYAxis().setLabel("Valor");

        XYChart.Series openSerie = new XYChart.Series();
        openSerie.setName("Open");
        XYChart.Series highSerie = new XYChart.Series();
        highSerie.setName("High");
        XYChart.Series closeSerie = new XYChart.Series();
        closeSerie.setName("Close");
        XYChart.Series lowSerie = new XYChart.Series();
        lowSerie.setName("Low");
        for (int i = 0; i < movimientos.size(); i+=15) {
            openSerie.getData().add(new XYChart.Data(movimientos.get(i).getDate(), movimientos.get(i).getOpen()));
            highSerie.getData().add(new XYChart.Data(movimientos.get(i).getDate(), movimientos.get(i).getHigh()));
            closeSerie.getData().add(new XYChart.Data(movimientos.get(i).getDate(), movimientos.get(i).getClose()));
            lowSerie.getData().add(new XYChart.Data(movimientos.get(i).getDate(), movimientos.get(i).getLow()));
        }



        lineChart.getData().addAll(openSerie,highSerie,closeSerie,lowSerie);

    }


    public void errAction(ActionEvent actionEvent) {
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText("Este es un error provocado, como prueba del funcionamiento de los modales como error. \n " +
                "Tambien funciona, si al documento de carga se le modifica la ruta");
        a.show();

    }

    public void promedio(ActionEvent actionEvent) {
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setContentText("El promedio de los precios de cerrado es: " + prom());
        a.show();
    }

    private double prom(){
        return movimientos.stream()
                .map(movimiento -> movimiento.getClose())
                .reduce((double)0,(sum,prom) -> sum + prom)/movimientos.size();
    }

    public void desviacion(ActionEvent actionEvent) {
        double promedio = prom();
        double varianza = movimientos.stream()
                .map(movimiento -> Math.pow(movimiento.getClose()-promedio,2))
                .reduce((double)0,(sum,prom) -> sum+prom)/movimientos.size()-1;
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.setContentText("la desviacion estandar del precio de cierre es: " + Math.sqrt(varianza));
        a.show();
    }

    public String[][] extremos() {
        String[] mayor= new String[2];
        String[] menor= new String[2];
        mayor[1] = "0";
        menor[1] = String.valueOf(prom());


        BufferedReader bufferLectura = null;
        try {
            bufferLectura = new BufferedReader(new FileReader("docs/BTC-USD.csv"));
            String linea = bufferLectura.readLine();
            linea = bufferLectura.readLine();
            while (linea != null) {
                String[] campos = linea.split(",");
                for (int j = 1; j < campos.length-1 ; j++) {
                    if (Double.parseDouble(mayor[1]) < Double.parseDouble(campos[j])) {
                        mayor[0]= campos[0];
                        mayor[1]= campos[j];
                    }
                    if (Double.parseDouble(menor[1]) > Double.parseDouble(campos[j])) {
                        menor[0]= campos[0];
                        menor[1]= campos[j];
                    }
                }
                linea = bufferLectura.readLine();
            }

        }
        catch (IOException e) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("El error es: "+e.getMessage());
            a.show();

        }
        finally {
            if (bufferLectura != null) {
                try {
                    bufferLectura.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new String[][] {mayor,menor};
    }

    public void menor(ActionEvent actionEvent) {
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.setContentText("La fecha del menor valor del Bitcoin es: " +  extremos()[1][0] +" Su valor: " + extremos()[1][1] );
        a.show();
    }

    public void mayor(ActionEvent actionEvent) {
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.setContentText("La fecha del mayor valor del Bitcoin es: " +  extremos()[0][0] +" Su valor: " + extremos()[0][1] );
        a.show();

    }
}