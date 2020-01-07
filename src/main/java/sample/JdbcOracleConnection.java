package sample;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcOracleConnection {

    private static final String  beginingBrace = "(";
    private static final String finalBrace = ")";
    private static final String activityForMicrobiology = "696";
    private static final String getQueryForInstrumentSRCD ="SELECT service_resource_cd FROM lab_instrument WHERE instr_name =";
    private static final String commonAndOperatorForSrLi = "AND sr.service_resource_cd = li.service_resource_cd";
    private static final String queryForInstrumentName = "SELECT instr_name FROM lab_instrument "
            + "WHERE service_resource_cd IN "
            + "(select SERVICE_RESOURCE_CD from SERVICE_RESOURCE sr where sr.ACTIVITY_TYPE_CD="
            +activityForMicrobiology
            + ")";
    private static final String queryForDispatchDownload = "SELECT sr.dispatch_download_ind FROM service_resource sr,"
            + "lab_instrument li WHERE li.instr_name='";
    private static final String queryForCollectedDownload = "SELECT sr.collected_download_ind FROM service_resource sr,"
            + "lab_instrument li WHERE li.instr_name='";
    private static final String queryForR_Mode = "SELECT sr.oper_mode FROM service_resource sr,"
            + "lab_instrument li WHERE li.instr_name='";
    private static final String queryForOrderableNames = "SELECT cv.display_key FROM code_value cv WHERE cv.code_value " +
            "IN" +beginingBrace+"select orl.catalog_cd from orc_resource_list orl where orl.service_resource_cd=";


    //select cv.display_key from orc_resource_list orl, code_value cv plan orl where orl.service_resource_cd=728798119 join cv where cv.code_value = orl.catalog_cd

    Connection conn = null;
    Statement stmtInstName = null;
    Statement stmtDispatchDownload = null;
    Statement stmtCollectedDownload = null;
    Statement stmtR_Mode = null;
    Statement stmtOrderableName = null;
    Statement stmtGetSrcdForInstName = null;

    //Connection String
    private static final String dbURL2 = "jdbc:oracle:thin:@ipsurrounddb.ip.devcerner.net:1521:surd1";
    private static final String username = "v500";
    private static final String password = "v500";

    protected ResultSet rsInstName;
    protected ResultSet getResultSize;
    protected ResultSet rsDispDown;
    protected ResultSet rsCollDown;
    protected ResultSet rsR_Mode;
    protected ResultSet rsOrderables;
    protected ResultSet rsSrcdInstName;


    int resultSetSize = 0;
    int index = 0;

    String[] instrumentNameArray ;
    String[] orderablesNameArray;
    String dispDownResult;
    String collDownResult;
    String rModeResut;
    String srcdFromInstName;

    public void makeDatabaseConnection() {

        //Driver Registration
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Establishing Connection
        try {
            conn = DriverManager.getConnection(dbURL2, username, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (conn != null) {
            //System.out.println("Connected to Database Surround");
        }
    }

    public void getResultSize(){
        try {
            //Querying Database

            stmtInstName = conn.createStatement();

            boolean statusForInstName = stmtInstName.execute(queryForInstrumentName);
            if(statusForInstName){
                //query is a select query.
                getResultSize = stmtInstName.executeQuery(queryForInstrumentName);

                while(getResultSize.next()) {
                    resultSetSize++;
                }
                getResultSize.close();
            } else {
                //query can be update or any query apart from select query
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void executeQueryInstrumentName(){

        try {
            //Querying Database
            getResultSize();
            instrumentNameArray = new String[resultSetSize];
            stmtInstName = conn.createStatement();

            boolean statusForInstName = stmtInstName.execute(queryForInstrumentName);
            if(statusForInstName){
                //query is a select query.
                rsInstName = stmtInstName.executeQuery(queryForInstrumentName);

                while(rsInstName.next()) {
                        instrumentNameArray[index] = rsInstName.getString(1);
                    index++;
                }
                rsInstName.close();
            } else {
                //query can be update or any query apart from select query
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean executeQueryDispDown(String instrumentName) {
            boolean returnValueDispDown;
        try {
            stmtDispatchDownload = conn.createStatement();
            //query is a select query.
                rsDispDown = stmtDispatchDownload.executeQuery(queryForDispatchDownload+instrumentName+"'"+ commonAndOperatorForSrLi);
                while(rsDispDown.next()){
                    dispDownResult = rsDispDown.getString(1);
                    //System.out.println("Display Downoad Result :"+dispDownResult);
                }
                rsDispDown.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        if(dispDownResult.equals("1")){
            returnValueDispDown = true;
        }else{
            returnValueDispDown = false;
        }
        return  returnValueDispDown;
    }

    public boolean executeQueryCollDown(String instrumentName) {
            boolean retrunValueCollDown;
        try {
            stmtCollectedDownload = conn.createStatement();
                //query is a select query.
                rsCollDown = stmtCollectedDownload.executeQuery(queryForCollectedDownload+instrumentName+"'"+ commonAndOperatorForSrLi);
                while(rsCollDown.next()){
                    collDownResult = rsCollDown.getString(1);
                    //System.out.println("Collected Download Result : "+collDownResult);
                }
                rsCollDown.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        if(collDownResult.equals("1")){
            retrunValueCollDown = true;
        }else{
            retrunValueCollDown = false;
        }
        return retrunValueCollDown;
    }

    public boolean executeQueryR_Mode(String instrumentName) {
        boolean retrunValueR_Mode;
        try {
            stmtR_Mode = conn.createStatement();

                rsR_Mode = stmtR_Mode.executeQuery(queryForR_Mode+instrumentName+"'"+ commonAndOperatorForSrLi);
                while(rsR_Mode.next()){
                   rModeResut = rsR_Mode.getString(1);
                    //System.out.println("R Mode Result : "+rModeResut);
                }

                rsR_Mode.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        if(rModeResut.equals("B")){
            retrunValueR_Mode = true;
        }else{
            retrunValueR_Mode = false;
        }
        return retrunValueR_Mode;
    }

    public void getSrcdOfInstName(String instrumentName){
        try{
            stmtGetSrcdForInstName = conn.createStatement();
            boolean statusForInstName = stmtGetSrcdForInstName.execute(getQueryForInstrumentSRCD+"'"+instrumentName+"'");
            if(statusForInstName){
                //query is a select query.
                rsSrcdInstName = stmtGetSrcdForInstName.executeQuery(getQueryForInstrumentSRCD+"'"+instrumentName+"'");
                while (rsSrcdInstName.next()){
                    srcdFromInstName = rsSrcdInstName.getString(1);
                   // System.out.println("The SRCD for Instrument "+instrumentName+" is "+srcdFromInstName);
                }
                rsSrcdInstName.close();
            } else {
                //query can be update or any query apart from select query
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void executeQueryOrderableNames(){
        try {
            //Querying Database
            getResultSize();
            orderablesNameArray = new String[resultSetSize];

            stmtOrderableName = conn.createStatement();

            boolean statusForOrderableName = stmtOrderableName.execute(queryForOrderableNames+srcdFromInstName+finalBrace);
            if(statusForOrderableName){
                //query is a select query.
                rsOrderables = stmtOrderableName.executeQuery(queryForOrderableNames+srcdFromInstName+finalBrace);

                while(rsOrderables.next()) {
                    orderablesNameArray[index] = rsOrderables.getString(1);
                    index++;
                }
                rsOrderables.close();
            } else {
                //query can be update or any query apart from select query
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
