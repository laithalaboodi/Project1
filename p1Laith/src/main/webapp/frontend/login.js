async function loginSubmit(event){
    //TODO
    //make tickets more informative
    //fix spacing with csss
    //make buttons not look trash
    event.preventDefault();
    
    let username = document.getElementById("username").value
    let password = document.getElementById("password").value
    
    const credentials = {
        username,
        password
    }
    
    try{
        let res = await fetch("http://localhost:8080/p1/login", {
            method:"POST",
            body: JSON.stringify(credentials),
            headers:{
                "Content-Type" : "application/json"
            },
        })

        if(res.status === 404){
            alert("User Not Found")
            throw "User not Found"
        }
        
        let user = await res.json()
        
        console.log(user);

        if(user.role === "EMPLOYEE"){
            document.location.href = "employeeHomeScreen.html"
        }else if(user.role === "FINANCE_MANAGER"){
            document.location.href = "financeManager.html"
        }else{
            console.log("error")
        }
    } catch (e) {
        console.log(e);
    }
    
}

document.getElementById("loginForm").addEventListener('submit', loginSubmit)