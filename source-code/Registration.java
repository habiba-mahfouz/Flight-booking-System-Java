package dsproject;

public class Registration {

    // linked list for users data
    Single_LL users = new Single_LL();

    // data base for users
    public void DataSet() {
        String[] user_1 = {"Ahmed", "ahmed@gmail.com", "01548892366", "ahmed@123" ,"24" , "male" , "123456789" , "30301010101010"} ;
        users.append(user_1);
        String[] user_2 = {"Ali", "ali@gmail.com", "01053892066", "ali@123" ,"24" , "male" , "123456778" , "30301010101011"};
        users.append(user_2);
        String[] user_3 = {"Noha", "noha@gmail.com", "01257892966", "noha@123" ,"24" , "female" , "123456677" , "30301010101012"};
        users.append(user_3);
    }

    // log in
    public int Login(String identifier, String password) {

        String[] user = {identifier, password};
        return users.LoginSearch(user);
        /*
        return 0 : no account
        return 1 : wrong password or identifier
        return 2 : you are logged in 
         */
    }

    // sign up
    public int Signup(String name, String email, String phone_no, String password, String age, String gender, String NationalID, String Passport_no) {
        int is_signedup = 0;
        int success = 0;

        // check if password == confirm password 
        is_signedup = 1;
        
        String[] user = {
        name, email, phone_no, password,
        age != null ? age : "",
        gender != null ? gender : "",
        NationalID != null ? NationalID : "",
        Passport_no != null ? Passport_no : ""
        };

        success = users.SignupSearch(user);

        // If checks passed (success is 0), add the user
        if (success == 0) {
            users.append(user);
        }

        return (is_signedup + success);
    }
    
    // check up if all data exist
    public boolean checkData (String [] data)
    {
        // data order:
        // 0:name, 1:email, 2:phone, 3:password, 4:age, 5:gender, 6:NationalID, 7:Passport_no
        Boolean all_completed = true ;
        for (int i = 4; i < data.length; i++) {
            if (data[i] == null || data[i].trim().isEmpty()) {
                all_completed = false;
                break;
            }
        }
        return all_completed ;
    }

    // get user data by identifier to use it after login / signup
    public String[] getUserData(String identifier) {
        return users.findUser(identifier);
    }
}

