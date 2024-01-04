package com.example.qlcc;

import com.example.qlcc.DataModel.*;
import com.example.qlcc.DatabaseConnector.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kotlin.Pair;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class dashboardController implements Initializable {

    CurrentUserData userData = HelloController.userData;
    Connection connect;
    PreparedStatement prepare;
    ResultSet result;
    Statement statement;

    /**
     * Available Form
     * **/
    @FXML
    private ComboBox<String> Activity_hostname;

    @FXML
    private TextField Activity_Description;

    @FXML
    private DatePicker Activity_date;

    @FXML
    private TextField Activity_location;

    @FXML
    private TextField Activity_name;

    @FXML
    private TextField Activity_search;

    @FXML
    private TextField Expense_amount;

    @FXML
    private Button Avaiable_btn;

    @FXML
    private TextField Expense_category;

    @FXML
    private TableColumn<?, ?> Expense_col_amount;

    @FXML
    private TableColumn<?,?> Expense_col_endDate;

    @FXML
    private TableColumn<?, ?> Expense_col_amount1;

    @FXML
    private TableColumn<?, ?> Expense_col_category;

    @FXML
    private TableColumn<?, ?> Expense_col_category1;

    @FXML
    private TableColumn<?, ?> Expense_col_date;

    @FXML
    private TableColumn<?, ?> Expense_col_date1;

    @FXML
    private TableColumn<?, ?> Expense_col_description1;

    @FXML
    private TableColumn<?, ?> Expense_col_purpose;

    @FXML
    private TableColumn<?, ?> Expense_col_purpose1;

    @FXML
    private TextField Expense_description;

    @FXML
    private AnchorPane Available_form;

    @FXML
    private TextField Expense_purpose;

    @FXML
    private TextField Expense_search;

    @FXML
    private TableView<ExpenseData> Expense_table;

    @FXML
    private TableView<ActivityData> Expense_table1;

    @FXML
    private Button Activity_btn;

    @FXML
    private AnchorPane Activity_form;

    @FXML
    private Button Borrow_btn;

    @FXML
    private AnchorPane Household_form;

    @FXML
    private Button Household_btn;

    @FXML
    private AnchorPane Resident_form;

    @FXML
    private Button Logout_btn;

    @FXML
    private CheckBox SettingForm_PassWordHiddencheckbox;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password_field_password;

    @FXML
    private PasswordField password_field_reenter;

    @FXML
    private Button setting_btn;

    @FXML
    private AnchorPane setting_form;

    @FXML
    private TextField text_field_password;

    @FXML
    private TextField text_field_reenter;

    @FXML
    private ComboBox<String> Activity_hostID;

    /**
     * Activity Form
     * **/

    @FXML
    private DatePicker Activity_endDate;
    public ObservableList<ActivityData> addActivityListData(){

        ObservableList<ActivityData> listAvailable = FXCollections.observableArrayList();

        String sql = "SELECT activity.*, resident.Name FROM activity LEFT JOIN resident" +
                " ON HostID = residentID";

        connect = Database.connectDB();

        try{
            ActivityData expenseD;
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                expenseD = new ActivityData(result.getInt(1),
                        result.getString(2),
                        result.getString(8),
                        result.getString(5),
                        result.getString(4),
                        result.getDate(3),
                        result.getString(6),
                        result.getDate(7));
                listAvailable.add(expenseD);
            }

        }catch(Exception e){e.printStackTrace(System.out);}

        return listAvailable;
    }
    private ObservableList<ActivityData> addActivityListD;

    public void ActivitySearch(){
        FilteredList<ActivityData> filter = new FilteredList<>(addActivityListD, e->true);

        Activity_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateActivityData ->{

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if(predicateActivityData.getName().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateActivityData.getHostname().toLowerCase().contains(searchKey)){
                    return true;
                }else if (String.valueOf(predicateActivityData.getDate()).contains(searchKey)){
                    return true;}
                else if (String.valueOf(predicateActivityData.getEndDate()).contains(searchKey)){
                    return true;
                }
                else return predicateActivityData.getLocation().toLowerCase().contains(searchKey);
            });
            SortedList <ActivityData> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(Expense_table1.comparatorProperty());
            Expense_table1.setItems(sortList);
        });
    }

    public void addActivityShowListData(){
        addActivityListD = addActivityListData();

        Expense_col_purpose1.setCellValueFactory(new PropertyValueFactory<>("name"));
        Expense_col_category1.setCellValueFactory(new PropertyValueFactory<>("hostname"));
        Expense_col_amount1.setCellValueFactory(new PropertyValueFactory<>("location"));
        Expense_col_date1.setCellValueFactory(new PropertyValueFactory<>("date"));
        Expense_col_description1.setCellValueFactory(new PropertyValueFactory<>("description"));
        Expense_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        Expense_table1.setItems(addActivityListD);
    }

    private int Number = -1;

    public void addActivitySelect(){
        ActivityClear();
        ActivityData expenseD = Expense_table1.getSelectionModel().getSelectedItem();
        int num = Expense_table1.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        Activity_name.setText(expenseD.getName());

        Activity_hostname.getSelectionModel().clearSelection();
        Activity_hostname.setValue(expenseD.getHostname());
        Activity_location.setText(expenseD.getLocation());
        Activity_date.setValue(LocalDate.parse(String.valueOf(expenseD.getDate())));
        Activity_Description.setText(expenseD.getDescription());
        Activity_endDate.setValue(LocalDate.parse(String.valueOf(expenseD.getEndDate())));

        Activity_hostID.getSelectionModel().clearSelection();
        Activity_hostID.setValue(expenseD.getHostID());
        Number = expenseD.getId();
    }

    public void setActivityHostNameList()
    {
        setHostNameList();

        ObservableList<String> ObserveList = FXCollections.observableArrayList(HostName);

        Activity_hostname.setItems(ObserveList);
    }

    public void addActivityHostIDSelection(){
        String sql = "SELECT residentID FROM resident WHERE Name = '" + Activity_hostname.getSelectionModel().getSelectedItem()
                + "' AND role = 'HouseOwner'";

        HostId.clear();

        connect = Database.connectDB();

        try{
            assert  connect != null;
            prepare = connect.prepareStatement(sql);

            result = prepare.executeQuery();

            while (result.next())
            {
                HostId.add(result.getString(1));
            }

            ObservableList<String> ObserveList = FXCollections.observableArrayList(HostId);

            Activity_hostID.setItems(ObserveList);

            if (HostId.size() == 1)
            {
                Activity_hostID.getSelectionModel().select(0);
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    private final List<String> HostName = new ArrayList<>();
    private final List<String> HostId = new ArrayList<>();

    public void ActivityAdd() {

        setHostNameList();

        String insertData = "INSERT INTO activity (Name,Date,Location,HostID, note,endDate) VALUES (?,?,?,?,?,?)";

        connect = Database.connectDB();
        try{
            Alert alert;
            if (Activity_name.getText().isEmpty()
                    || Activity_location.getText().isEmpty()
                    || Activity_hostname.getSelectionModel().getSelectedItem() == null
                    || Activity_date.getValue() == null
                    || Activity_endDate.getValue() == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else if (Number != -1)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please use update function instead");
                alert.showAndWait();
            }else
            {
                //String UN = userData.getUsername();
                String des;
                if (Activity_Description.getText().isEmpty()) des = "No Description";
                else des = Activity_Description.getText();
                prepare = connect.prepareStatement(insertData);
                prepare.setString(1,Activity_name.getText());
                prepare.setString(2,String.valueOf(Activity_date.getValue()));
                prepare.setString(3,Activity_location.getText());
                prepare.setString(4,Activity_hostID.getSelectionModel().getSelectedItem());
                prepare.setString(5,des);
                prepare.setString(6,String.valueOf(Activity_endDate.getValue()));
                //prepare.setString(6,UN);

                prepare.executeUpdate();

                addActivityShowListData();

                ActivityClear();
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void ActivityClear(){
        Activity_name.setText("");
        Activity_hostname.getSelectionModel().clearSelection();
        Activity_location.setText("");
        Activity_date.setValue(null);
        Activity_Description.setText("");
        Activity_endDate.setValue(null);
        Number = -1;
    }

    public void ActivityUpdate(){
        String NumNow = String.valueOf(Number);
        setHostNameList();
        String des = Activity_Description.getText();
        if (Activity_Description.getText().isEmpty()) des = "No Description";

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Activity_name.getText().isEmpty()
                    || Activity_location.getText().isEmpty()
                    || Activity_hostname.getSelectionModel().getSelectedItem() == null
                    || Activity_date.getValue() == null
                    || Activity_endDate.getValue() == null
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data");
                alert.showAndWait();
            } else {
                String updateData = "UPDATE activity SET "
                        + "Name = '" + Activity_name.getText()
                        + "', Date = '" + Activity_date.getValue()
                        + "', endDate = '" + Activity_endDate.getValue()
                        + "', Location = '" + Activity_location.getText()
                        + "', HostID = '" + Activity_hostID.getSelectionModel().getSelectedItem()
                        + "', note = '" + des
                        + "' WHERE ID = "
                        + NumNow;

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE data  " + NumNow + "?");
                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(updateData);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addActivityShowListData();
                    // TO CLEAR THE FIELDS
                    ActivityClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void ActivityDelete(){
        String NumNow = String.valueOf(Number);
        String deleteData = "DELETE FROM activity WHERE ID = " + NumNow;

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Activity_name.getText().isEmpty()
                    || Activity_location.getText().isEmpty()
                    || Activity_date.getValue() == null
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data to delete");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE this data (#" + NumNow + ")?");

                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {

                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);

                    String checkData = "SELECT ID FROM activity "
                            + "WHERE ID = " + NumNow;

                    prepare = connect.prepareStatement(checkData);
                    result = prepare.executeQuery();

                    // IF THE STUDENT NUMBER IS EXIST THEN PROCEED TO DELETE
                    if (result.next()) {
                        String deleteGrade = "DELETE FROM activity WHERE "
                                + "ID = " + NumNow;

                        statement = connect.createStatement();
                        statement.executeUpdate(deleteGrade);

                    }// IF NOT THEN NVM

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addActivityShowListData();
                    // TO CLEAR THE FIELDS
                    ActivityClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


    /***
    * Available Form
    * */
    public ObservableList<ExpenseData> addExpenseListData(){

        ObservableList<ExpenseData> listAvailable = FXCollections.observableArrayList();

        String sql = "SELECT * FROM available";

        connect = Database.connectDB();

        try{
            ExpenseData expenseD;
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                expenseD = new ExpenseData(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4),
                        result.getInt(5),
                        result.getString(6));
                listAvailable.add(expenseD);
            }

        }catch(Exception e){e.printStackTrace(System.out);}

        return listAvailable;
    }
    private ObservableList<ExpenseData> addAvailableListD;
    public void ExpenseSearch(){
        FilteredList<ExpenseData> filter = new FilteredList<>(addAvailableListD, e->true);

        Expense_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateExpenseData ->{

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if(predicateExpenseData.getName().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateExpenseData.getCategory().toLowerCase().contains(searchKey)){
                    return true;
                } else return predicateExpenseData.getDescription().toLowerCase().contains(searchKey);
            });
            SortedList <ExpenseData> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(Expense_table.comparatorProperty());
            Expense_table.setItems(sortList);
        });
    }

    public void addExpenseShowListData(){
        addAvailableListD = addExpenseListData();

        //Expense_col_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        Expense_col_purpose.setCellValueFactory(new PropertyValueFactory<>("name"));
        Expense_col_category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        Expense_col_amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        Expense_col_date.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        //Expense_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));

        Expense_table.setItems(addAvailableListD);
    }

    public void addExpenseSelect(){
        ExpenseData expenseD = Expense_table.getSelectionModel().getSelectedItem();
        int num = Expense_table.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        Expense_purpose.setText(expenseD.getName());
        Expense_amount.setText(String.valueOf(expenseD.getMaxAmount()));
        Expense_category.setText(expenseD.getCategory());
        Expense_description.setText(expenseD.getDescription());
        Number = expenseD.getNo();
    }

    public void ExpenseAdd() {
        connect = Database.connectDB();
        try{
            Alert alert;
            if (Expense_purpose.getText().isEmpty()
                    || Expense_category.getText().isEmpty()
                    || Expense_amount.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else if (Number != -1)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please use update function instead");
                alert.showAndWait();
            }else
            {
                String checkSql = "SELECT ID FROM available WHERE Name = '" +
                        Expense_purpose.getText() + "'";

                PreparedStatement CheckExistence = connect.prepareStatement(checkSql);
                result = CheckExistence.executeQuery();

                if (result.next())
                {
                    String addData = "UPDATE available SET Available = Available +"  + Expense_amount.getText() +
                            " , MaxAmount = MaxAmount  + " + Expense_amount.getText() + " WHERE ID = " + result.getString(1);

                    prepare = connect.prepareStatement(addData);
                    prepare.executeUpdate();

                } else {
                    String insertData = "INSERT INTO available (Name,Type,Available,MaxAmount, note) VALUES (?,?,?,?,?)";
                    String des;
                    if (Expense_description.getText().isEmpty()) des = "No Description";
                    else des = Expense_description.getText();
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, Expense_purpose.getText());
                    prepare.setString(2, Expense_category.getText());
                    prepare.setInt(3, Integer.parseInt(Expense_amount.getText()));
                    prepare.setInt(4, Integer.parseInt(Expense_amount.getText()));
                    prepare.setString(5, des);

                    prepare.executeUpdate();
                }

                addExpenseShowListData();
                ExpenseClear();
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void ExpenseClear(){
        Expense_purpose.setText("");
        Expense_category.setText("");
        Expense_amount.setText("");
        Expense_description.setText("");
        Number = -1;
    }

    public void ExpenseUpdate(){
        String NumNow = String.valueOf(Number);
        ExpenseData expenseD = Expense_table.getSelectionModel().getSelectedItem();
        int currentAmount = (expenseD.getAmount() - (expenseD.getMaxAmount() - Integer.parseInt(Expense_amount.getText())));

        String des = Expense_description.getText();
        if (Expense_description.getText().isEmpty()) des = "No Description";
        String updateData = "UPDATE available SET "
                + "Name = '" + Expense_purpose.getText()
                + "', Type = '" + Expense_category.getText()
                + "', MaxAmount = " + Expense_amount.getText()
                + ", Available = " + currentAmount
                + ", note = '" + des
                + "' WHERE ID = "
                + NumNow;

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Expense_purpose.getText().isEmpty()
                    || Expense_amount.getText().isEmpty()
                    || Expense_category.getText().isEmpty()
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data");
                alert.showAndWait();
            } else if (currentAmount < 0)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Available amount doesn't enough to update!\n Please return facility and update");
                alert.showAndWait();
            }
            {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE data  " + NumNow + "?");
                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(updateData);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addExpenseShowListData();
                    // TO CLEAR THE FIELDS
                    ExpenseClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void ExpenseDelete(){
        String NumNow = String.valueOf(Number);
        String deleteData = "DELETE FROM available WHERE ID = " + NumNow;
        ExpenseData expenseD = Expense_table.getSelectionModel().getSelectedItem();

        //System.out.println(expenseD.getAmount());

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Expense_purpose.getText().isEmpty()
                    || Expense_amount.getText().isEmpty()
                    || Expense_category.getText().isEmpty()
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data to delete");
                alert.showAndWait();
            } else if (expenseD.getMaxAmount() != expenseD.getAmount()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please return all selected facility to delete to delete");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE this data (#" + NumNow + ")?");

                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {

                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);

                    String checkData = "SELECT ID FROM available "
                            + "WHERE ID = " + NumNow;

                    prepare = connect.prepareStatement(checkData);
                    result = prepare.executeQuery();

                    // IF THE STUDENT NUMBER IS EXIST THEN PROCEED TO DELETE
                    if (result.next()) {
                        String deleteGrade = "DELETE FROM available WHERE "
                                + "ID = " + NumNow;

                        statement = connect.createStatement();
                        statement.executeUpdate(deleteGrade);

                    }// IF NOT THEN NVM

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addExpenseShowListData();
                    // TO CLEAR THE FIELDS
                    ExpenseClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /******
     *  Borrowed Form
     * *********/
    @FXML
    private ComboBox<String> Borrow_FacName;

    @FXML
    private ComboBox<String> Borrow_activity;

    @FXML
    private TextField Borrow_amount;

    @FXML
    private TableColumn<?, ?> Borrow_col_activity;

    @FXML
    private TableColumn<?, ?> Borrow_col_amount;

    @FXML
    private TableColumn<?, ?> Borrow_col_date1;

    @FXML
    private TableColumn<?, ?> Borrow_col_date2;

    @FXML
    private TableColumn<?, ?> Borrow_col_hostname;

    @FXML
    private TableColumn<?, ?> Borrow_col_name;

    @FXML
    private DatePicker Borrow_date1;

    @FXML
    private DatePicker Borrow_date2;

    @FXML
    private AnchorPane Borrow_form;

    @FXML
    private ComboBox<String> Borrow_hostname;

    @FXML
    private TextField Borrow_search;

    @FXML
    private TableView<BorrowData> Borrow_table;

    public void BorrowReUpdateStatus() {
        String sql = "UPDATE borrow SET status = CASE " +
                "WHEN ReturnDate < CAST(Now() AS date) THEN 'Expired' " +
                "WHEN BorrowingDate < CAST(Now() AS date) THEN 'In Progress' " +
                "ELSE 'In queue' END";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);

            prepare.executeUpdate();

            addBorrowShowListData();
        } catch (SQLException e){
            e.printStackTrace(System.out);
        }

    }

    public void setBorrowTableColor(){
        BorrowReUpdateStatus();
        Borrow_table.getStylesheets().add(Objects.requireNonNull(getClass().getResource("highlightedCell.css")).toExternalForm());

        Borrow_table.setRowFactory(tv -> new TableRow<>(){
            @Override
            protected void updateItem (BorrowData item, boolean empty){
                super.updateItem(item, empty);

                if (item == null || empty){
                    setStyle("");
                } else {
                    LocalDate rowDate = LocalDate.parse(String.valueOf(item.getBorrowingDate()));
                    LocalDate returnDate = LocalDate.parse(String.valueOf(item.getReturnDate()));

                    LocalDate currentDate = LocalDate.now();

                    if (returnDate.isBefore(currentDate)){
                        getStyleClass().add("red-row");
                    } else if (rowDate.isBefore(currentDate)){
                        getStyleClass().add("green-row");
                    }
                    else {
                        getStyleClass().remove("red-row");
                        getStyleClass().remove("green-row");
                        setStyle("");
                    }
                }
            }
        });
    }

    public void addBorrowFacilitiesOnSelection(){
        setFacilityList();

        ObservableList<String> ObserveList = FXCollections.observableArrayList(FacilityName);

        Borrow_FacName.setItems(ObserveList);
    }

    public ObservableList<BorrowData> addBorrowListData(){

        ObservableList<BorrowData> listAvailable = FXCollections.observableArrayList();

        String sql = "SELECT borrow.*, Resident.Name, Activity.Name " +
                "FROM borrow " +
                "LEFT JOIN resident ON HostID = residentID " +
                "LEFT JOIN activity ON ActivityID = Activity.ID";

        connect = Database.connectDB();

        try{
            BorrowData expenseD;
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                expenseD = new BorrowData(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getDate(4),
                        result.getDate(5),
                        result.getInt(6),
                        result.getInt(7),
                        result.getInt(8),
                        result.getString(9),
                        result.getString(10),
                        result.getString(11));
                listAvailable.add(expenseD);
            }

        }catch(Exception e){e.printStackTrace(System.out);}

        return listAvailable;
    }
    private ObservableList<BorrowData> addBorrowListD;

    public void BorrowSearch(){
        FilteredList<BorrowData> filter = new FilteredList<>(addBorrowListD, e->true);

        Borrow_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateBorrowData ->{

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if(predicateBorrowData.getFacilityName().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateBorrowData.getHostName().toLowerCase().contains(searchKey)){
                    return true;
                }else if (String.valueOf(predicateBorrowData.getBorrowingDate()).contains(searchKey)){
                    return true;}
                else if (String.valueOf(predicateBorrowData.getReturnDate()).contains(searchKey)){
                    return true;}
                else return predicateBorrowData.getActivityName().toLowerCase().contains(searchKey);
            });
            SortedList <BorrowData> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(Borrow_table.comparatorProperty());
            Borrow_table.setItems(sortList);
        });
    }

    //TODO add another hostID combo box
    public void addBorrowShowListData(){
        addBorrowListD = addBorrowListData();

        Borrow_col_name.setCellValueFactory(new PropertyValueFactory<>("FacilityName"));
        Borrow_col_hostname.setCellValueFactory(new PropertyValueFactory<>("HostName"));
        Borrow_col_amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        Borrow_col_date1.setCellValueFactory(new PropertyValueFactory<>("BorrowingDate"));
        Borrow_col_date2.setCellValueFactory(new PropertyValueFactory<>("ReturnDate"));
        Borrow_col_activity.setCellValueFactory(new PropertyValueFactory<>("ActivityName"));

        Borrow_table.setItems(addBorrowListD);
    }

    public void addBorrowSelect(){
        BorrowData expenseD = Borrow_table.getSelectionModel().getSelectedItem();
        int num = Borrow_table.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;


        Borrow_FacName.setValue(expenseD.getFacilityName());
        //Expense_amount.setText(String.valueOf(expenseD.getAmount()));
        Borrow_hostname.getSelectionModel().clearSelection();
        Borrow_hostname.setValue(expenseD.getHostName());
        Borrow_amount.setText(String.valueOf(expenseD.getAmount()));

        Borrow_activity.getSelectionModel().clearSelection();
        Borrow_activity.setValue(expenseD.getActivityName());

        Borrow_date1.setValue(LocalDate.parse(String.valueOf(expenseD.getBorrowingDate())));
        Borrow_date2.setValue(LocalDate.parse(String.valueOf(expenseD.getReturnDate())));

        Number = expenseD.getBorrowID();
    }

    //TODO rewrite setHostNameList
    public void setHostNameList()
    {
        HostName.clear();
        HostId.clear();
        String sql = "SELECT Name, residentID FROM resident WHERE role = 'HouseOwner'";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                HostName.add(result.getString(1));
                HostId.add(result.getString(2));
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void setBorrowHostNameList()
    {
        setHostNameList();

        ObservableList<String> ObserveList = FXCollections.observableArrayList(HostName);

        Borrow_hostname.setItems(ObserveList);
    }

    public void setActivityList()
    {
        ActivityName.clear();
        ActivityID.clear();
        String sql = "SELECT ID, Name FROM activity";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                ActivityName.add(result.getString(2));
                ActivityID.add(result.getInt(1));
            }

        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void setBorrowActivityList()
    {
        setActivityList();

        ObservableList<String> ObserveList = FXCollections.observableArrayList(ActivityName);

        Borrow_activity.setItems(ObserveList);

        //Borrow_activity.getEditor().textProperty().bindBidirectional();
    }

    public void addBorrowerIDSelection(){
        //BorrowData borrowD = Borrow_table.getSelectionModel().getSelectedItem();
        String sql = "Select residentID from resident WHERE Name = '" + Borrow_hostname.getSelectionModel().getSelectedItem() + "'";

        HostId.clear();

        connect = Database.connectDB();

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);

            result = prepare.executeQuery();

            while (result.next()){
                HostId.add(result.getString(1));
            }

            ObservableList<String> ObserveList = FXCollections.observableArrayList(HostId);

            Borrow_hostID.setItems(ObserveList);

            if (HostId.size() == 1) Borrow_hostID.getSelectionModel().select(0);
        } catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

    public void setFacilityList()
    {
        FacilityID.clear();
        FacilityName.clear();
        String sql = "SELECT ID, Name FROM available";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                FacilityName.add(result.getString(2));
                FacilityID.add(result.getInt(1));
                //System.out.println(result.getString(2));
            }

        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    private final List<String> FacilityName = new ArrayList<>();
    private final List<Integer> FacilityID = new ArrayList<>();

    private final List<String> ActivityName = new ArrayList<>();
    private final List<Integer> ActivityID = new ArrayList<>();

    @FXML
    private ComboBox<String> Borrow_hostID;

    //TODO add borrow means minus facility, check if the facility is enough to borrow
    public void BorrowAdd() {

        String insertData = "INSERT INTO borrow (Name,HostID,BorrowingDate,ReturnDate,ActivityID,FacilityID, Amount) VALUES (?,?,?,?,?,?,?)";

        setHostNameList();
        setActivityList();

        connect = Database.connectDB();
        try{
            setFacilityList();
            String checkDataSql = "SELECT Available FROM available WHERE ID = " +
                    FacilityID.get(FacilityName.indexOf(Borrow_FacName.getSelectionModel().getSelectedItem()));

            PreparedStatement checkData = connect.prepareStatement(checkDataSql);

            ResultSet checkResult = checkData.executeQuery();

            checkResult.next();
            int AmountLeft = checkResult.getInt(1);

            Alert alert;
            if (Borrow_FacName.getSelectionModel().getSelectedItem() == null
                    || Borrow_amount.getText().isEmpty()
                    || Borrow_hostname.getSelectionModel().getSelectedItem() == null
                    //|| Borrow_activity.getSelectionModel().getSelectedItem() == null
                    || Borrow_date1.getValue() == null
                    || Borrow_date2.getValue() == null){

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else if (Number != -1)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please use update function instead");
                alert.showAndWait();
            } else if (AmountLeft < Integer.parseInt(Borrow_amount.getText()))
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("The quantity of this facility currently not enough");
                alert.showAndWait();
            } else
            {
                setHostNameList();
                //String UN = userData.getUsername();
                prepare = connect.prepareStatement(insertData);
                prepare.setString(1,Borrow_FacName.getSelectionModel().getSelectedItem());
                prepare.setString(2, HostId.get(HostName.indexOf(Borrow_hostname.getSelectionModel().getSelectedItem())));
                prepare.setString(3,String.valueOf(Borrow_date1.getValue()));
                prepare.setString(4,String.valueOf(Borrow_date2.getValue()));
                prepare.setInt(5, ActivityID.get(ActivityName.indexOf(Borrow_activity.getSelectionModel().getSelectedItem())));
                prepare.setInt(6, FacilityID.get(FacilityName.indexOf(Borrow_FacName.getSelectionModel().getSelectedItem())));
                prepare.setInt(7, Integer.parseInt(Borrow_amount.getText()));

                //prepare.setString(6,UN);

                prepare.executeUpdate();

                String updateFacilityDataSql = "UPDATE available SET Available = Available - " + Borrow_amount.getText()
                        + " WHERE ID = " + FacilityID.get(FacilityName.indexOf(Borrow_FacName.getSelectionModel().getSelectedItem()));
                //System.out.println(updateFacilityDataSql);

                PreparedStatement updateFacility = connect.prepareStatement(updateFacilityDataSql);

                updateFacility.executeUpdate();

                addBorrowShowListData();

                BorrowClear();
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void BorrowClear(){
        Borrow_FacName.setValue(null);
        Borrow_hostname.getSelectionModel().clearSelection();
        Borrow_amount.setText("");
        Borrow_activity.getSelectionModel().clearSelection();
        Borrow_date1.setValue(null);
        Borrow_date2.setValue(null);
        Number = -1;
    }

    public void BorrowUpdate(){
        String NumNow = String.valueOf(Number);
        BorrowData borrowD = Borrow_table.getSelectionModel().getSelectedItem();

        connect = Database.connectDB();
        setFacilityList();
        setActivityList();

        try {
            Alert alert;
            if (Borrow_FacName.getSelectionModel().getSelectedItem() == null
                    || Borrow_amount.getText().isEmpty()
                    || Borrow_hostname.getSelectionModel().getSelectedItem() == null
                    || Borrow_activity.getSelectionModel().getSelectedItem() == null
                    || Borrow_date1.getValue() == null
                    || Borrow_date2.getValue() == null
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data");
                alert.showAndWait();
            } else {
                String updateData = "UPDATE borrow SET "
                        + "Name = '" + Borrow_FacName.getSelectionModel().getSelectedItem()
                        + "', HostID = " + HostId.get(HostName.indexOf(Borrow_hostname.getSelectionModel().getSelectedItem()))
                        + " , BorrowingDate = '" + Borrow_date1.getValue()
                        + "' , ReturnDate = '" + Borrow_date2.getValue()
                        + "', ActivityID = " + ActivityID.get(ActivityName.indexOf(Borrow_activity.getSelectionModel().getSelectedItem()))
                        + " , FacilityID = " + FacilityID.get(FacilityName.indexOf(Borrow_FacName.getSelectionModel().getSelectedItem()))
                        + " , Amount = " + Borrow_amount.getText()
                        + " WHERE BorrowID = "
                        + NumNow;

                String returnCurrentFacilitySql = "UPDATE available SET " +
                        "Available = Available + " + borrowD.getAmount() + " WHERE ID = " +
                        borrowD.getFacilityID();

                String borrowNewFacilitySql = "UPDATE available SET " +
                        "Available = Available - " + Borrow_amount.getText() + " WHERE ID = " +
                        FacilityID.get(FacilityName.indexOf(Borrow_FacName.getSelectionModel().getSelectedItem()));

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE data ?");
                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {

                    PreparedStatement returnCurrentFacility = connect.prepareStatement(returnCurrentFacilitySql);
                    returnCurrentFacility.executeUpdate();

                    PreparedStatement borrowNewFacility = connect.prepareStatement(borrowNewFacilitySql);
                    borrowNewFacility.executeUpdate();

                    statement = connect.createStatement();
                    statement.executeUpdate(updateData);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addBorrowShowListData();
                    // TO CLEAR THE FIELDS
                    BorrowClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void BorrowDelete(){
        String NumNow = String.valueOf(Number);
        BorrowData borrowD = Borrow_table.getSelectionModel().getSelectedItem();
        String deleteData = "DELETE FROM borrow WHERE BorrowID = " + NumNow;

        setFacilityList();
        connect = Database.connectDB();

        Alert alert;
        if (Borrow_FacName.getSelectionModel().getSelectedItem() == null
                || Borrow_amount.getText().isEmpty()
                || Borrow_hostname.getSelectionModel().getSelectedItem() == null
                || Borrow_activity.getSelectionModel().getSelectedItem() == null
                || Borrow_date1.getValue() == null
                || Borrow_date2.getValue() == null
                || Number == -1) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a data to delete");
            alert.showAndWait();

            } else {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE this data ?");
            Optional<ButtonType> option = alert.showAndWait();

            assert option.orElse(null) != null;
            if (option.orElse(null).equals(ButtonType.OK)) {

                Dialog<Pair<Integer,Integer>> returnDialog = new Dialog<>();
                returnDialog.setTitle("Input a number");
                returnDialog.setHeaderText(null);

                Label returnLabel = new Label ("Input number of returned Facility");
                TextField returnTextField = new TextField();
                returnDialog.getDialogPane().setContent(new VBox(returnLabel,returnTextField));

                Label brokenLabel = new Label ("Input number of broken Facility");
                TextField brokenTextField = new TextField();
                returnDialog.getDialogPane().setContent(new VBox(returnLabel,returnTextField, brokenLabel, brokenTextField));

                returnDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                returnDialog.setResultConverter(buttonType -> {
                    if (buttonType == ButtonType.OK)
                    {
                        String input1 = returnTextField.getText();
                        String input2 = brokenTextField.getText();
                        try {
                            int number1 = Integer.parseInt(input1);
                            int number2 = Integer.parseInt(input2);
                            return new Pair<>(number1,number2);
                        } catch (NumberFormatException e)
                        {
                            return null;
                        }
                    }
                    return null;
                    });

                    returnDialog.showAndWait().ifPresent(result -> {
                        if (result != null) {
                            int number1 = result.getFirst();
                            int number2 = result.getSecond();
                            // Sử dụng số liệu, ví dụ: in ra console

                            String updateFacilitySql = "UPDATE available SET Available = Available + " +
                                    number1 + ", MaxAmount = MaxAmount - " + number2 + " WHERE ID = " +
                                    borrowD.getFacilityID();

                            String updateBorrowSql = "UPDATE borrow SET Amount = Amount - " + (number1 + number2) + " WHERE BorrowID = " +
                                    borrowD.getBorrowID();

                            if (number1 + number2 <= borrowD.getAmount()) {
                                try {
                                    PreparedStatement updateFacility = connect.prepareStatement(updateFacilitySql);
                                    updateFacility.executeUpdate();

                                    if (number1 + number2 == borrowD.getAmount()) {
                                        statement = connect.createStatement();
                                        statement.executeUpdate(deleteData);

                                        String checkData = "SELECT BorrowID FROM borrow "
                                                + "WHERE BorrowID = " + NumNow;

                                        prepare = connect.prepareStatement(checkData);
                                        this.result = prepare.executeQuery();

                                        // IF THE STUDENT NUMBER IS EXIST THEN PROCEED TO DELETE
                                        if (this.result.next()) {
                                            String deleteGrade = "DELETE FROM borrow WHERE "
                                                    + "BorrowID = " + NumNow;

                                            statement = connect.createStatement();
                                            statement.executeUpdate(deleteGrade);

                                        }// IF NOT THEN NVM

                                        Alert alert2;
                                        alert2 = new Alert(Alert.AlertType.INFORMATION);
                                        alert2.setTitle("Information Message");
                                        alert2.setHeaderText(null);
                                        alert2.setContentText("Successfully Deleted!");
                                        alert2.showAndWait();
                                    } else {
                                        //System.out.println(updateBorrowSql);
                                        PreparedStatement updateBorrow = connect.prepareStatement(updateBorrowSql);
                                        updateBorrow.executeUpdate();

                                        Alert alert2;
                                        alert2 = new Alert(Alert.AlertType.INFORMATION);
                                        alert2.setTitle("Information Message");
                                        alert2.setHeaderText(null);
                                        alert2.setContentText("Successfully return " + number1 + " Facilities/Facility");
                                        alert2.showAndWait();
                                    }

                                    // TO UPDATE THE TABLEVIEW
                                    addBorrowShowListData();
                                    // TO CLEAR THE FIELDS
                                    BorrowClear();
                                } catch (Exception e) {
                                    e.printStackTrace(System.out);
                                }
                            } else {
                                Alert alert2;
                                alert2 = new Alert(Alert.AlertType.ERROR);
                                alert2.setTitle("Information Message");
                                alert2.setHeaderText(null);
                                alert2.setContentText("Please type in a valid number!");
                                alert2.showAndWait();
                            }
                        }
                    });
                }
            }
    }

    /*****
     * Household Form
     * *****/

    @FXML
    private TableColumn<?, ?> House_col_name;

    @FXML
    private TableColumn<?, ?> House_col_num;

    @FXML
    private TableColumn<?, ?> House_col_owner;

    @FXML
    private TextField House_name;

    @FXML
    private TextField House_num;

    @FXML
    private ComboBox<String> House_owner;

    @FXML
    private ComboBox<String> House_ownerID;

    @FXML
    private TextField House_search;

    @FXML
    private TableView<HouseholdData> House_table;

    //TODO ad owner and ownerID combo box. ownerID combo box hasn't included
    List <String> ResidentName = new ArrayList<>();
    List <String> ResidentID = new ArrayList<>();

    public void addResidentList(int SelectedHouse){
        String sql = "SELECT Name FROM resident WHERE HouseID = " + SelectedHouse;
        ResidentName.clear();

        connect = Database.connectDB();
        try {
            assert  connect != null;
            prepare = connect.prepareStatement(sql);

            result = prepare.executeQuery();
            while (result.next())
            {
                ResidentName.add(result.getString(1));
            }
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void addHouseResidentSelection(int Selection)
    {
        addResidentList(Selection);

        ObservableList<String> ObserveList = FXCollections.observableArrayList(ResidentName);

        House_owner.setItems(ObserveList);
    }

    public ObservableList<HouseholdData> addHouseholdListData(){

        ObservableList<HouseholdData> listAvailable = FXCollections.observableArrayList();

        String sql = "SELECT * FROM household";

        connect = Database.connectDB();

        try{
            HouseholdData expenseD;
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                expenseD = new HouseholdData(result.getInt(1),
                        result.getInt(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5));
                listAvailable.add(expenseD);
            }

        }catch(Exception e){e.printStackTrace(System.out);}

        return listAvailable;
    }
    private ObservableList<HouseholdData> addHouseListD;
    public void HouseSearch(){
        FilteredList<HouseholdData> filter = new FilteredList<>(addHouseListD, e->true);

        House_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateHouseholdData ->{

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if(predicateHouseholdData.getHouseName().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateHouseholdData.getHouseOwner().toLowerCase().contains(searchKey)){
                    return true;
                } else return String.valueOf(predicateHouseholdData.getResidentNum()).contains(searchKey);
            });
            SortedList <HouseholdData> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(House_table.comparatorProperty());
            House_table.setItems(sortList);
        });
    }

    public void addHouseShowListData(){
        addHouseListD = addHouseholdListData();

        House_col_name.setCellValueFactory(new PropertyValueFactory<>("HouseName"));
        House_col_num.setCellValueFactory(new PropertyValueFactory<>("residentNum"));
        House_col_owner.setCellValueFactory(new PropertyValueFactory<>("HouseOwner"));

        House_table.setItems(addHouseListD);
    }

    public void addResidentIDList(String name)
    {
        String sql = "SELECT residentID FROM resident WHERE Name = '" + name + "'";

        ResidentID.clear();
        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);

            result = prepare.executeQuery();
            while (result.next())
            {
                ResidentID.add(result.getString(1));
            }
        }catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void addHouseResidentIDSelection()
    {
        String name = House_owner.getSelectionModel().getSelectedItem();

        addResidentIDList(name);

        ObservableList<String> ObserveList = FXCollections.observableArrayList(ResidentID);

        House_ownerID.setItems(ObserveList);

        if (ResidentID.size() == 1)
        {
            House_ownerID.getSelectionModel().select(0);
        }
    }

    public void addHouseSelect(){
        HouseholdData expenseD = House_table.getSelectionModel().getSelectedItem();
        int num = House_table.getSelectionModel().getSelectedIndex();

        addHouseResidentSelection(expenseD.getHouseID());

        if ((num - 1) < -1) return;

        House_name.setText(expenseD.getHouseName());
        House_num.setText(String.valueOf(expenseD.getResidentNum()));

        House_owner.getSelectionModel().clearSelection();
        House_ownerID.getSelectionModel().clearSelection();

        House_owner.setValue(expenseD.getHouseOwner());
        House_ownerID.setValue(expenseD.getHouseOwnerID());

        CurrentHouseData = expenseD;
        Number = expenseD.getHouseID();
    }

    public void HouseClear(){
        House_name.setText("");
        House_num.setText("");
        House_owner.getSelectionModel().clearSelection();
        House_ownerID.getSelectionModel().clearSelection();

        CurrentHouseData = null;
        Number = -1;
    }

    private HouseholdData CurrentHouseData;

    public void HouseUpdate(){
        HouseholdData HouseD = House_table.getSelectionModel().getSelectedItem();
        String NumNow = String.valueOf(Number);

        String updateData = "UPDATE household SET "
                + "housename = '" + House_name.getText()
                + "', Houseowner = '" + House_owner.getSelectionModel().getSelectedItem()
                + "', residentNum = " + House_num.getText()
                + ", ownerID = '" + House_ownerID.getSelectionModel().getSelectedItem()
                + "' WHERE HouseID = "
                + NumNow;

        connect = Database.connectDB();

        try {
            Alert alert;
            if (House_name.getText().isEmpty()
                    || House_num.getText().isEmpty()
                    || House_owner.getSelectionModel().getSelectedItem() == null
                    || House_ownerID.getSelectionModel().getSelectedItem() == null
                    || Number == -1) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE data ?");
                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;

                if (option.orElse(null).equals(ButtonType.OK)) {
                    if (!HouseD.getHouseOwner().equals(House_owner.getSelectionModel().getSelectedItem())
                       || !HouseD.getHouseOwnerID().equals(House_ownerID.getSelectionModel().getSelectedItem()))
                    {
                        //if house owner change, change the resident list
                        String sql = "UPDATE resident SET " +
                                "role = CASE WHEN residentID = '" + House_ownerID.getSelectionModel().getSelectedItem() + "' THEN 'HouseOwner' " +
                                            "WHEN residentID = '" + CurrentHouseData.getHouseOwnerID() + "' THEN 'Member' " +
                                            "ELSE residentID END";

                        PreparedStatement PreProcess = connect.prepareStatement(sql);

                        PreProcess.executeUpdate();
                    }

                    statement = connect.createStatement();
                    statement.executeUpdate(updateData);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();


                    // TO UPDATE THE TABLEVIEW
                    addHouseShowListData();
                    // TO CLEAR THE FIELDS
                    HouseClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void HouseDelete(){
        String NumNow = String.valueOf(Number);
        String deleteData = "DELETE FROM household WHERE HouseID = " + NumNow;

        connect = Database.connectDB();

        try {
            Alert alert;
            if (House_name.getText().isEmpty()
                    || House_num.getText().isEmpty()
                    || House_owner.getSelectionModel().getSelectedItem() == null
                    || House_ownerID.getSelectionModel().getSelectedItem() == null
                    || Number == -1) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data to delete");
                alert.showAndWait();
            } else if (CurrentHouseData.getResidentNum() != 0 &&
                      !CurrentHouseData.getHouseOwnerID().equals("null"))
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a appropriate data (an empty house) to delete");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE this data?");

                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {


                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);

                    String checkData = "SELECT HouseID FROM household "
                            + "WHERE HouseID = " + NumNow;

                    prepare = connect.prepareStatement(checkData);
                    result = prepare.executeQuery();

                    // IF THE STUDENT NUMBER IS EXIST THEN PROCEED TO DELETE
                    if (result.next()) {
                        String deleteGrade = "DELETE FROM household WHERE "
                                + "HouseID = " + NumNow;

                        statement = connect.createStatement();
                        statement.executeUpdate(deleteGrade);

                    }// IF NOT THEN NVM

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addHouseShowListData();
                    // TO CLEAR THE FIELDS
                    HouseClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /******
     * Resident Form
     * ********/

    @FXML
    private TextField Resident_ID;

    @FXML
    private TextField Resident_address;

    @FXML
    private TableColumn<?,?> Resident_col_name;

    @FXML
    private TableColumn<?, ?> Resident_col_address;

    @FXML
    private TableColumn<?, ?> Resident_col_dob;

    @FXML
    private TableColumn<?, ?> Resident_col_gender;

    @FXML
    private TableColumn<?, ?> Resident_col_hometown;

    @FXML
    private TableColumn<?, ?> Resident_col_house;

    @FXML
    private TableColumn<?, ?> Resident_col_nation;

    @FXML
    private TableColumn<?, ?> Resident_col_role;

    @FXML
    private DatePicker Resident_dob;

    @FXML
    private TextField Resident_gender;

    @FXML
    private TextField Resident_home;

    @FXML
    private TextField Resident_house1;

    @FXML
    private ComboBox<String> Resident_house2;

    @FXML
    private TextField Resident_name;

    @FXML
    private TextField Resident_nation;

    @FXML
    private ComboBox<String> Resident_role;

    @FXML
    private TextField Resident_search;

    @FXML
    private TableView<ResidentData> Resident_table;

    @FXML
    private ImageView Resident_image;

    public ObservableList<ResidentData> addResidentListData(){

        ObservableList<ResidentData> listAvailable = FXCollections.observableArrayList();

        String sql = "SELECT resident.*, housename FROM resident " +
                "LEFT JOIN household ON resident.HouseID = household.HouseID";

        connect = Database.connectDB();

        try{
            ResidentData expenseD;

            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                //System.out.println(result.getString(1));
                expenseD = new ResidentData(result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getDate(5),
                        result.getString(6),
                        result.getString(7),
                        result.getString(8),
                        result.getInt(9),
                        result.getString(10),
                        result.getString(11));
                listAvailable.add(expenseD);
            }

        }catch(Exception e){e.printStackTrace(System.out);}

        return listAvailable;
    }
    private ObservableList<ResidentData> addResidentListD;
    public void ResidentSearch(){
        FilteredList<ResidentData> filter = new FilteredList<>(addResidentListD, e->true);

        Resident_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateResidentData ->{

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if(predicateResidentData.getName().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateResidentData.getHometown().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateResidentData.getPermanentAddress().toLowerCase().contains(searchKey)){
                    return true;
                }else if (String.valueOf(predicateResidentData.getBirth()).toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateResidentData.getGender().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateResidentData.getNation().toLowerCase().contains(searchKey)){
                    return true;
                } else if (predicateResidentData.getRole().toLowerCase().contains(searchKey)){
                    return true;
                }else return predicateResidentData.getHouseName().toLowerCase().contains(searchKey);
            });
            SortedList <ResidentData> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(Resident_table.comparatorProperty());
            Resident_table.setItems(sortList);
        });
    }

    public void addResidentShowListData(){
        addResidentListD = addResidentListData();

        Resident_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Resident_col_dob.setCellValueFactory(new PropertyValueFactory<>("birth"));
        Resident_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        Resident_col_hometown.setCellValueFactory(new PropertyValueFactory<>("hometown"));
        Resident_col_address.setCellValueFactory(new PropertyValueFactory<>("permanentAddress"));
        Resident_col_nation.setCellValueFactory(new PropertyValueFactory<>("nation"));
        Resident_col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        Resident_col_house.setCellValueFactory(new PropertyValueFactory<>("HouseName"));

        Resident_table.setItems(addResidentListD);
    }

    private String resID, ImgPath;
    private Image image;
    private ResidentData PickedResidentData;

    public void addResidentSelect(){
        ResidentData expenseD = Resident_table.getSelectionModel().getSelectedItem();
        int num = Resident_table.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        Resident_name.setText(expenseD.getName());
        Resident_dob.setValue(LocalDate.parse(String.valueOf(expenseD.getBirth())));
        Resident_gender.setText(expenseD.getGender());
        Resident_home.setText(expenseD.getHometown());
        Resident_address.setText(expenseD.getPermanentAddress());
        Resident_ID.setText(expenseD.getResidentID());
        Resident_nation.setText(expenseD.getNation());

        Resident_role.getSelectionModel().clearSelection();
        Resident_role.setValue(expenseD.getRole());

        //(expenseD.getResidentID());

        Resident_house1.setText(expenseD.getHouseName());
        Resident_house2.getSelectionModel().clearSelection();
        Resident_house2.setValue(expenseD.getHouseName());

        String uri = "file:" + expenseD.getImage();

        image = new Image(uri, 135, 179, false, true);

        Resident_image.setImage(image);

        ImgPath = expenseD.getImage();
        Number = expenseD.getHouseID();
        resID = expenseD.getResidentID();
        PickedResidentData = expenseD;
    }

    public void setHouseNameOnRoleSelection()
    {
        if (Resident_role.getSelectionModel().getSelectedItem() == null) return;
        if (Resident_role.getSelectionModel().getSelectedItem().equals("Member")){
            Resident_house1.setVisible(false);
            Resident_house2.setVisible(true);
        } else
        {
            Resident_house2.setVisible(false);
            Resident_house1.setVisible(true);
        }
    }

    public String setDefaultImageDestination(){
        URL locationURL = getClass().getResource("dashboard.fxml");
        String location = "";
        if (locationURL != null){
            location = locationURL.toExternalForm();
            location = location.replace("qlcc/dashboard.fxml","Image/");
            location = location.replace("file:/", "");
            location = location.replace("/", "\\");
            //System.out.println(location);
        }
        return location;
    }

    public void CopyInsertedImage()
    {
        //Copy file into destination
        String destinationDirectory = setDefaultImageDestination();

        System.out.println(destinationDirectory);

        try{
            Path Source = Path.of(ImgPath);

            Path destination = Path.of(destinationDirectory, Source.getFileName().toString());

            Files.copy(Source, destination, StandardCopyOption.REPLACE_EXISTING);

            ImgPath = destination.toString();

        }catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

    public void addResidentInsertImage() {

        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            image = new Image(file.toURI().toString(), 120, 149, false, true);
            Resident_image.setImage(image);

            ImgPath = file.getAbsolutePath();
        }
    }

    private final List<String> Role = new ArrayList<>();

    public void addRoleList(){
        String sql = "Select Role FROM attribute where role = 'HouseOwner' or role = 'Member' ";
        Role.clear();
        connect = Database.connectDB();
        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while(result.next())
            {
                Role.add(result.getString(1));
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void addResidentRoleSelection(){
        addRoleList();

        ObservableList<String> ObserveList = FXCollections.observableArrayList(Role);

        Resident_role.setItems(ObserveList);
    }

    private final List<String> HouseName = new ArrayList<>();

    private final List<Integer> HouseID = new ArrayList<>();

    public void addHouseList()
    {
        HouseName.clear();
        HouseID.clear();

        String sql = "Select HouseID, housename FROM household ";
        connect = Database.connectDB();
        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while(result.next())
            {
                HouseID.add(result.getInt(1));
                HouseName.add(result.getString(2));
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    private final String Default_female_path = "D:\\IdeaProjects\\qlcc\\src\\main\\resources\\com\\example\\Image\\default-female-profile.jpg";
    private final String Default_male_path = "D:\\IdeaProjects\\qlcc\\src\\main\\resources\\com\\example\\Image\\default-male-profile.jpg";

    public void addResidentHouseSelection(){
        addHouseList();

        ObservableList<String> ObserveList = FXCollections.observableArrayList(HouseName);

        Resident_house2.setItems(ObserveList);
    }

    public void ResidentAdd() {

        String insertData = "INSERT INTO resident (Name,hometown,permaAddress," +
                                                   "dob,gender,nation,role,houseID,image) " +
                                                   "VALUES (?,?,?,?,?,?,?,?,?)";

        connect = Database.connectDB();
        try{
            Alert alert;
            if (Resident_name.getText().isEmpty()
                    || Resident_dob.getValue() == null
                    || Resident_gender.getText().isEmpty()
                    || Resident_home.getText().isEmpty()
                    || Resident_nation.getText().isEmpty()
                    || Resident_address.getText().isEmpty()
                    || Resident_role.getSelectionModel().getSelectedItem() == null
                    || (Resident_house1.getText().isEmpty() && Resident_house2.getSelectionModel().getSelectedItem() == null)){

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else if (Number != -1)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please use update function instead");
                alert.showAndWait();
            }else
            {
                //ResultSet GenerateKey;
                int setHouse = -1;
                if(Resident_role.getSelectionModel().getSelectedItem().equals("Member")){
                    setHouse = HouseID.get(HouseName.indexOf(Resident_house2.getSelectionModel().getSelectedItem()));

                    String updateHouseSql = "UPDATE household SET residentnum = residentnum + 1 " +
                            "WHERE houseID = " + HouseID.get(HouseName.indexOf(Resident_house2.getSelectionModel().getSelectedItem())) ;

                    PreparedStatement PrePrepare = connect.prepareStatement(updateHouseSql);

                    //PrePrepare.setInt(1,HouseID.get(HouseName.indexOf(Resident_house2.getSelectionModel().getSelectedItem())));

                    PrePrepare.executeUpdate();

                }else {
                    //prepare.setString(8,Resident_house1.getText());

                    String updateHouseSql = "INSERT INTO household (residentNum, Houseowner, housename,ownerID) " +
                            "VALUES (1,?,?,'tmp')";

                    // = Database.connectDB();
                    PreparedStatement PrePrepare = connect.prepareStatement(updateHouseSql,Statement.RETURN_GENERATED_KEYS);
                    PrePrepare.setString(2, Resident_house1.getText());
                    PrePrepare.setString(1, Resident_name.getText());

                    int rowAffected = PrePrepare.executeUpdate();

                    if (rowAffected > 0){
                        ResultSet getGenKey = PrePrepare.getGeneratedKeys();

                        if (getGenKey.next())
                        {
                            setHouse = getGenKey.getInt(1);
                        }
                    }
                }

                prepare = connect.prepareStatement(insertData,Statement.RETURN_GENERATED_KEYS);

                prepare.setString(1,Resident_name.getText());
                prepare.setString(2,Resident_home.getText());
                prepare.setString(3,Resident_address.getText());
                prepare.setString(4,String.valueOf(Resident_dob.getValue()));
                prepare.setString(5,Resident_gender.getText());
                prepare.setString(6,Resident_nation.getText());
                prepare.setString(7,Resident_role.getSelectionModel().getSelectedItem());
                prepare.setInt(8,setHouse);

                if (ImgPath == null){
                    if (Resident_gender.getText().equals("Male")) ImgPath = Default_male_path;
                    else ImgPath = Default_female_path;
                } else {
                    CopyInsertedImage();
                }

                String uri = ImgPath;
                uri = uri.replace("\\", "\\\\");
                prepare.setString(9, uri);

                int row_affected = prepare.executeUpdate();
                String genKey = "";

                if (row_affected > 0)
                {
                    result  = prepare.getGeneratedKeys();

                    if (result.next()) {
                        genKey = result.getString(1);
                    }
                }
                //System.out.println(genKey);
                if (Resident_role.getSelectionModel().getSelectedItem().equals("HouseOwner"))
                {
                    addResidentHouseSelection();

                    //TODO rewrite the update
                    String sql = "UPDATE household SET ownerID = " +
                            "(SELECT residentID FROM resident " +
                            "WHERE Name = '" + Resident_name.getText() +"' AND " +
                            "hometown = '" + Resident_home.getText() + "' AND " +
                            "permaAddress = '" + Resident_address.getText() + "' AND " +
                            "dob = '" + Resident_dob.getValue() + "' AND " +
                            "gender = '" + Resident_gender.getText() + "' AND " +
                            "nation = '" + Resident_nation.getText() + "') " +
                            "Where HouseID = " +setHouse;

                    prepare = connect.prepareStatement(sql);

                    //System.out.println(prepare.toString());

                    prepare.executeUpdate();
                }
                addResidentShowListData();

                ResidentClear();
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void ResidentClear(){
        Resident_name.setText("");
        Resident_dob.setValue(null);
        Resident_gender.setText("");
        Resident_home.setText("");
        Resident_address.setText("");
        Resident_ID.setText("");
        Resident_nation.setText("");
        Resident_role.getSelectionModel().clearSelection();
        Resident_house2.getSelectionModel().clearSelection();
        Resident_house1.setText("");
        Resident_image.setImage(null);

        ImgPath = null;
        Number = -1;
        resID = "";
        PickedResidentData = null;
    }


    //TODO
    public void ResidentUpdate(){

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Resident_name.getText().isEmpty()
                    || Resident_dob.getValue() == null
                    || Resident_gender.getText().isEmpty()
                    || Resident_home.getText().isEmpty()
                    || Resident_nation.getText().isEmpty()
                    || Resident_address.getText().isEmpty()
                    || Resident_role.getSelectionModel().getSelectedItem() == null
                    || (Resident_house1.getText().isEmpty() && Resident_house2.getSelectionModel().getSelectedItem() == null)
                    || PickedResidentData == null) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data");
                alert.showAndWait();
            } else {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE data ?");
                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;

                if (option.orElse(null).equals(ButtonType.OK)) {

                    int setHouse;
                    if (ImgPath == null){
                        if (Resident_gender.getText().equals("Male")) ImgPath = Default_male_path;
                        else ImgPath = Default_female_path;
                    }else {
                        CopyInsertedImage();
                    }

                    String uri = ImgPath;
                    uri = uri.replace("\\", "\\\\");

                    setHouse = -1;

                    if (Resident_role.getSelectionModel().getSelectedItem().equals("Member")){
                        setHouse = HouseID.get(HouseName.indexOf(Resident_house2.getSelectionModel().getSelectedItem()));

                        if (PickedResidentData.getRole().equals("Member"))// member -> member
                        {
                            if (!PickedResidentData.getHouseName().equals(Resident_house2.getSelectionModel().getSelectedItem()))
                            {
                                //TODO update data
                                String sql = "UPDATE household SET residentnum = CASE " +
                                        "WHEN houseID = "+ HouseID.get(HouseName.indexOf(Resident_house2.getSelectionModel().getSelectedItem())) + " THEN residentnum + 1 " +
                                        "WHEN houseID = "+ PickedResidentData.getHouseID() + " THEN residentnum - 1 " +
                                        "ELSE residentnum END " +
                                        "WHERE houseID IN ( " + PickedResidentData.getHouseID() + " , " + HouseID.get(HouseName.indexOf(Resident_house2.getSelectionModel().getSelectedItem())) + ")";

                                PreparedStatement updateHouseData = connect.prepareStatement(sql);

                                //System.out.println(sql);
                                //worked
                                updateHouseData.executeUpdate();
                            }
                        }
                        else if (PickedResidentData.getRole().equals("HouseOwner")) //house owner -> member
                        {
                            /* set the old House owner = 'null', +1 residentnum in new house, -1 in old house*/

                            String sql = "UPDATE household SET houseowner = CASE " +
                                    "WHEN houseID = " + PickedResidentData.getHouseID() + " THEN 'null' " +
                                    "ELSE houseowner END, " +
                                    "ownerID = CASE " +
                                    "WHEN houseID = " + PickedResidentData.getHouseID() + " THEN 'null' " +
                                    "ELSE ownerID END, " +
                                    "residentnum = CASE " +
                                    "WHEN houseID = " + HouseID.get(HouseName.indexOf(Resident_house2.getSelectionModel().getSelectedItem())) + " THEN residentnum + 1 " +
                                    "WHEN houseID = " + PickedResidentData.getHouseID() + " THEN residentnum - 1 " +
                                    "ELSE residentnum END ";

                            PreparedStatement updateHouseData = connect.prepareStatement(sql);

                            //quite good
                            //System.out.println(sql);
                            updateHouseData.executeUpdate();
                        }
                    } else { //if Role == 'HouseOwner', using Resident_house1
                        //TODO change setHouse to if else

                        if (PickedResidentData.getRole().equals("Member")) // member -> house owner
                        {
                            String checksql = "SELECT * FROM household WHERE housename = ?";
                            PreparedStatement checkPrep = connect.prepareStatement(checksql);
                            checkPrep.setString(1, Resident_house1.getText());

                            result = checkPrep.executeQuery();

                            if (!result.next())
                            {
                                //TODO if the house is not exist, add house and set owner
                                String sql = "INSERT INTO household (residentnum, Houseowner, housename, ownerID) " +
                                        " VALUES (0,?,?,?) ";

                                PreparedStatement updateHouseData = connect.prepareStatement(sql);

                                updateHouseData.setString(1, Resident_name.getText());
                                updateHouseData.setString(2, Resident_house1.getText());
                                updateHouseData.setString(3, Resident_ID.getText());

                                //TODO Update the setHouseList

                                //System.out.println(sql);
                                updateHouseData.executeUpdate();

                                addResidentHouseSelection();
                            }
                            else {
                                //TODO else set existed house owner to this person, set the old house owner to member
                                String sql = "UPDATE resident SET role = 'Member' WHERE " +
                                        " residentID = (SELECT ownerID FROM household WHERE houseID = "+ HouseID.get(HouseName.indexOf(Resident_house1.getText())) +")";
                                //System.out.println(sql);

                                PreparedStatement mmb = connect.prepareStatement(sql);
                                mmb.executeUpdate();

                                sql = "UPDATE household SET " +
                                        "Houseowner = CASE WHEN HouseID = " + HouseID.get(HouseName.indexOf(Resident_house1.getText())) + " THEN '" + Resident_name.getText() +
                                        "' ELSE Houseowner END, " +
                                        "ownerID = CASE WHEN HouseID = "  + HouseID.get(HouseName.indexOf(Resident_house1.getText())) + " THEN '" + Resident_ID.getText() +
                                        "' ELSE ownerID END ";
                                //then update
                                //System.out.println(sql);
                                PreparedStatement m2 = connect.prepareStatement(sql);
                                m2.executeUpdate();
                            }
                            if (!(PickedResidentData.getHouseID() == HouseID.get(HouseName.indexOf(Resident_house1.getText())))) {
                                String sql = "UPDATE household SET residentnum = CASE " +
                                        "WHEN houseID = " + HouseID.get(HouseName.indexOf(Resident_house1.getText())) + " THEN residentnum + 1 " +
                                        "WHEN houseID = " + PickedResidentData.getHouseID() + " THEN residentnum - 1 " +
                                        "ELSE residentnum END ";
                                PreparedStatement mmb = connect.prepareStatement(sql);
                                mmb.executeUpdate();
                            }
                            //TODO +1 new house, -1 old house
                            setHouse = HouseID.get(HouseName.indexOf(Resident_house1.getText()));
                        }
                        else //house owner -> house owner
                        {
                            String sql;
                            // if nothing change then nothing change
                            if (!PickedResidentData.getHouseName().equals(Resident_house1.getText())) {
                                //TODO set old house's owner to null;

                                String checksql = "SELECT * FROM household WHERE housename = ?";
                                PreparedStatement checkPrep = connect.prepareStatement(checksql);
                                checkPrep.setString(1, Resident_house1.getText());

                                /////
                                //System.out.println(checkPrep);
                                ////

                                result = checkPrep.executeQuery();

                                if (!result.next()) {
                                    sql = "INSERT INTO household (residentNum, Houseowner, housename, ownerID) " +
                                            " VALUES (0,'" + Resident_name.getText() + "','" + Resident_house1.getText() + "','" + Resident_ID.getText() + "')";

                                    //System.out.println(sql);

                                    PreparedStatement updateHouseData = connect.prepareStatement(sql);
                                    updateHouseData.executeUpdate();

                                    //TODO if house is not exist, add new house and set owner
                                    addResidentHouseSelection();

                                }else {
                                    sql = "UPDATE resident SET role = 'Member' WHERE " +
                                            " residentID = (SELECT ownerID FROM household WHERE houseID = "+ HouseID.get(HouseName.indexOf(Resident_house1.getText())) +")";
                                    //System.out.println(sql);
                                    PreparedStatement UpdateResidentData = connect.prepareStatement(sql);
                                    UpdateResidentData.executeUpdate();

                                    //TODO else change new house owner to this person, set the old house owner to member
                                    sql = "UPDATE household SET " +
                                            "Houseowner = CASE WHEN HouseID = " + HouseID.get(HouseName.indexOf(Resident_house1.getText())) + " THEN '" + Resident_name.getText() +
                                            "' ELSE Houseowner END, " +
                                            "ownerID = CASE WHEN HouseID = "  + HouseID.get(HouseName.indexOf(Resident_house1.getText())) + " THEN '" + Resident_ID.getText() +
                                            "' ELSE ownerID END ";
                                    //then update
                                    //System.out.println(sql);
                                    PreparedStatement UpdateHouseData = connect.prepareStatement(sql);
                                    UpdateHouseData.executeUpdate();


                                }
                                sql = "UPDATE household SET residentnum = CASE " +
                                        "WHEN houseID = "+ HouseID.get(HouseName.indexOf(Resident_house1.getText())) + " THEN residentnum + 1 " +
                                        "WHEN houseID = "+ PickedResidentData.getHouseID() + " THEN residentnum - 1 " +
                                        "ELSE residentnum END " ;
                                //System.out.println(sql);
                                PreparedStatement mmb = connect.prepareStatement(sql);
                                mmb.executeUpdate();

                                sql = "UPDATE household SET houseowner = 'null', ownerID = 'null' WHERE " +
                                        "houseID = " + PickedResidentData.getHouseID();
                                //System.out.println(sql);
                                PreparedStatement m2 = connect.prepareStatement(sql);
                                m2.executeUpdate();
                                //TODO +1 new  house, -1 old house
                            }
                        }
                        setHouse = HouseID.get(HouseName.indexOf(Resident_house1.getText()));
                    }


                    String updateData = "UPDATE resident SET "
                            + "Name = '" + Resident_name.getText()
                            + "', hometown = '" + Resident_home.getText()
                            + "', permaAddress = '" + Resident_address.getText()
                            + "', dob = '" + Resident_dob.getValue()
                            + "', gender = '" + Resident_gender.getText()
                            + "', nation = '" + Resident_nation.getText()
                            + "', role = '" + Resident_role.getSelectionModel().getSelectedItem()
                            + "', HouseID = " + setHouse
                            + ", image = '" + uri
                            + "' WHERE residentID = '"
                            + resID + "'";

                    statement = connect.createStatement();
                    statement.executeUpdate(updateData);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addResidentShowListData();
                    // TO CLEAR THE FIELDS
                    ResidentClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void ResidentDelete(){
        String deleteData = "DELETE FROM resident WHERE residentID = '" + resID + "'";

        connect = Database.connectDB();

        try {
            Alert alert;
            if (PickedResidentData == null) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data to delete");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE this data?");

                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {

                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);

                    //-1 member from house
                    String houseSql = "UPDATE household SET residentnum = CASE " +
                            //"WHEN houseID = "+ HouseID.get(HouseName.indexOf(Resident_house1.getText())) + " THEN residentnum + 1 " +
                            "WHEN houseID = "+ PickedResidentData.getHouseID() + " THEN residentnum - 1 " +
                            "ELSE residentnum END ";

                    //System.out.println(houseSql);

                    prepare = connect.prepareStatement(houseSql);
                    prepare.executeUpdate();

                    //if it is a house owner, set owner to null
                    if (PickedResidentData.getRole().equals("HouseOwner"))
                    {
                        houseSql = "UPDATE household SET houseowner = 'null',  ownerID = 'null' WHERE " +
                                "houseID = " + PickedResidentData.getHouseID();
                        prepare = connect.prepareStatement(houseSql);
                        prepare.executeUpdate();
                    }

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    addResidentHouseSelection();
                    // TO UPDATE THE TABLEVIEW
                    addResidentShowListData();
                    // TO CLEAR THE FIELDS
                    ResidentClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /***
     *  Setting Form
     * *****/

    public void CheckBoxHiddenPassword()
    {
        if(SettingForm_PassWordHiddencheckbox.isSelected()){
            text_field_password.setText(password_field_password.getText());
            text_field_reenter.setText(password_field_reenter.getText());
            password_field_password.setVisible(false);
            password_field_reenter.setVisible(false);
            text_field_reenter.setVisible(true);
            text_field_password.setVisible(true);
        } else{
            password_field_reenter.setText(text_field_reenter.getText());
            password_field_password.setText(text_field_password.getText());
            password_field_password.setVisible(true);
            password_field_reenter.setVisible(true);
            text_field_reenter.setVisible(false);
            text_field_password.setVisible(false);
        }
    }
    @FXML
    private TextField text_field_username;

    public void settingUsernameChange(){
        String sql = "UPDATE admin SET username = '" + text_field_username.getText() + "' " +
                "WHERE username = '" + HelloController.userData.getUsername() + "' ";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);

            prepare.executeUpdate();

            ShowUsername();
        }catch (SQLException e){
            e.printStackTrace(System.out);
        }
    }

    @FXML
    private Label Username;

    public void ShowUsername(){
        Username.setText(HelloController.userData.getUsername());
    }

    public void settingPasswordChange()
    {
        if (HelloController.userData.getUsername().equals("phongnhvp")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You cannot change admin password");
            alert.setTitle("Permission prevented");
            alert.setHeaderText(null);

            alert.showAndWait();
            return;
        }

        String ChangePasswordSql, TmpGetPassword;
        if (SettingForm_PassWordHiddencheckbox.isSelected()) {
            TmpGetPassword = text_field_password.getText();
        } else {
            TmpGetPassword = password_field_password.getText();
        }
        ChangePasswordSql = "UPDATE admin SET"
                + " password = '" + TmpGetPassword
                + "' WHERE username ='" + userData.getUsername() + "'";
        Alert alert;
        connect = Database.connectDB();

        try{
            if (((text_field_password.getText().isEmpty()
                    ||text_field_reenter.getText().isEmpty()) && SettingForm_PassWordHiddencheckbox.isSelected())
                    ||((password_field_password.getText().isEmpty()
                    ||password_field_reenter.getText().isEmpty()) && !SettingForm_PassWordHiddencheckbox.isSelected())){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please enter/re-enter your new password blank fields");
                alert.showAndWait();
            } else if ((text_field_reenter.getText().equals(text_field_password.getText()) && SettingForm_PassWordHiddencheckbox.isSelected())
                    ||(password_field_password.getText().equals(password_field_reenter.getText()) && !SettingForm_PassWordHiddencheckbox.isSelected()))
            {
                String sqlCheck = "SELECT password FROM admin WHERE username = ?";
                PreparedStatement prepareToCheck;
                prepareToCheck = connect.prepareStatement(sqlCheck);
                prepareToCheck.setString(1, userData.getUsername());

                result = prepareToCheck.executeQuery();
                result.next();
                if (result.getString("password").equals(TmpGetPassword))
                {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message!");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter a new password!");
                    alert.showAndWait();
                }
                else {
                    prepare = connect.prepareStatement(ChangePasswordSql);
                    prepare.executeUpdate();

                    if (!text_field_username.getText().isEmpty())
                        settingUsernameChange();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INFORMATION MESSAGE");
                    alert.setHeaderText(null);
                    alert.setContentText("You have successfully changed your password");
                    alert.showAndWait();
                }
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE");
                alert.setHeaderText(null);
                alert.setContentText("Your re-enter password doesn't match the previous one");
                alert.showAndWait();
            }
        }catch(Exception e) {
            e.printStackTrace(System.out);}
    }

    /**
     * General Function
     * */

    @FXML
    private Button resident_btn;

    private double x,y;
    public void logOut(){
        try {
            Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> option = alert.showAndWait();

            assert option.orElse(null) != null;
            if (option.orElse(null).equals(ButtonType.OK))
            {
                Logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                scene.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                scene.setOnMouseDragged((MouseEvent event) ->{
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                    stage.setOpacity(.8);
                });

                scene.setOnMouseReleased((MouseEvent event) -> stage.setOpacity(1));


                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {e.printStackTrace(System.out);}
    }
    private int CurrentNavigator = 1;
    public void switchForm(ActionEvent event){
        if (event.getSource() == Activity_btn)
        {
            Activity_form.setVisible(true);
            Available_form.setVisible(false);
            Borrow_form.setVisible(false);
            Household_form.setVisible(false);
            Resident_form.setVisible(false);
            setting_form.setVisible(false);

            Activity_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            Avaiable_btn.setStyle("-fx-background-color: transparent");
            Borrow_btn.setStyle("-fx-background-color: transparent");
            Household_btn.setStyle("-fx-background-color: transparent");
            resident_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color: transparent");

            CurrentNavigator = 1;

            setActivityHostNameList();
            addActivityShowListData();

            Number = -1;
        } else if (event.getSource() == Avaiable_btn)
        {
            Activity_form.setVisible(false);
            Available_form.setVisible(true);
            Household_form.setVisible(false);
            Resident_form.setVisible(false);
            Borrow_form.setVisible(false);
            setting_form.setVisible(false);

            Activity_btn.setStyle("-fx-background-color: transparent");
            Avaiable_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            Borrow_btn.setStyle("-fx-background-color: transparent");
            Household_btn.setStyle("-fx-background-color: transparent");
            resident_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color: transparent");

            CurrentNavigator = 2;

            Number = -1;
            addExpenseShowListData();
        } else if (event.getSource() == Borrow_btn)
        {
            Activity_form.setVisible(false);
            Available_form.setVisible(false);
            Borrow_form.setVisible(true);
            Household_form.setVisible(false);
            Resident_form.setVisible(false);
            setting_form.setVisible(false);

            Activity_btn.setStyle("-fx-background-color: transparent");
            Avaiable_btn.setStyle("-fx-background-color: transparent");
            Borrow_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            Household_btn.setStyle("-fx-background-color: transparent");
            resident_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color: transparent");

            addBorrowFacilitiesOnSelection();
            addBorrowShowListData();
            setBorrowActivityList();
            setBorrowHostNameList();

            Number = -1;
            CurrentNavigator = 3;

        } else if (event.getSource() == Household_btn)
        {
            Activity_form.setVisible(false);
            Available_form.setVisible(false);
            Borrow_form.setVisible(false);
            Household_form.setVisible(true);
            Resident_form.setVisible(false);
            setting_form.setVisible(false);

            Activity_btn.setStyle("-fx-background-color: transparent");
            Avaiable_btn.setStyle("-fx-background-color: transparent");
            Borrow_btn.setStyle("-fx-background-color: transparent");
            Household_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            resident_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color: transparent");

            CurrentNavigator = 4;
            Number = -1;
            addHouseShowListData();

        } else if (event.getSource() == resident_btn){
            Activity_form.setVisible(false);
            Available_form.setVisible(false);
            Borrow_form.setVisible(false);
            Household_form.setVisible(false);
            Resident_form.setVisible(true);
            setting_form.setVisible(false);

            Activity_btn.setStyle("-fx-background-color: transparent");
            Avaiable_btn.setStyle("-fx-background-color: transparent");
            Borrow_btn.setStyle("-fx-background-color: transparent");
            Household_btn.setStyle("-fx-background-color: transparent");
            resident_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            setting_btn.setStyle("-fx-background-color: transparent");

            addResidentRoleSelection();
            addResidentHouseSelection();
            addResidentShowListData();
            Number = -1;
            CurrentNavigator = 5;

        }
        else if (event.getSource() == setting_btn)
        {
            Activity_form.setVisible(false);
            Available_form.setVisible(false);
            Household_form.setVisible(false);
            Resident_form.setVisible(false);
            Borrow_form.setVisible(false);
            setting_form.setVisible(true);

            Activity_btn.setStyle("-fx-background-color: transparent");
            Avaiable_btn.setStyle("-fx-background-color: transparent");
            Borrow_btn.setStyle("-fx-background-color: transparent");
            Household_btn.setStyle("-fx-background-color: transparent");
            resident_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");

            CurrentNavigator = 6;

            text_field_password.setVisible(false);
            text_field_reenter.setVisible(false);
            SettingForm_PassWordHiddencheckbox.setSelected(false);
        }
    }

    public void setCloseAction()
    {
        System.exit(0);
    }

    public void Minimize()
    {
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    public void defaultNav(){
        //Navigate Home Form First
        Activity_form.setVisible(true);
        Available_form.setVisible(false);
        Household_form.setVisible(false);
        Resident_form.setVisible(false);
        Borrow_form.setVisible(false);
        setting_form.setVisible(false);

        Activity_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
        Avaiable_btn.setStyle("-fx-background-color: transparent");
        Borrow_btn.setStyle("-fx-background-color: transparent");
        Household_btn.setStyle("-fx-background-color: transparent");
        setting_btn.setStyle("-fx-background-color: transparent");

        setActivityHostNameList();
        addActivityShowListData();
        //Re-create hover css on navigate buttons
        Activity_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 1) return;
            Activity_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        Activity_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 1) return;
            Activity_btn.setStyle("-fx-background-color: transparent");
        });

        Avaiable_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 2) return;
            Avaiable_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        Avaiable_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 2) return;
            Avaiable_btn.setStyle("-fx-background-color: transparent");
        });

        Borrow_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 3) return;
            Borrow_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        Borrow_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 3) return;
            Borrow_btn.setStyle("-fx-background-color: transparent");
        });

        Household_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 4) return;
            Household_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        Household_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 4) return;
            Household_btn.setStyle("-fx-background-color: transparent");
        });

        setting_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 6) return;
            setting_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        setting_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 6) return;
            setting_btn.setStyle("-fx-background-color: transparent");
        });

        resident_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 5) return;
            resident_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        resident_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 5) return;
            resident_btn.setStyle("-fx-background-color: transparent");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defaultNav();

        setFacilityList();

        ShowUsername();
    }
}

// re-configure the UI alignment in resident form
//TODO change setting form
//TODO add end date to activity form
//TODO change UI of login form and remove auto fill username password