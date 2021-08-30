async function getAllTicketsByStatus(event){
    event && event.preventDefault()
    
    let url = "http://localhost:8080/p1/manager/"
    url += document.getElementById("filterSelect").value
    console.log(url)
    try{
        let res = await fetch(url, {
            method:"GET",
            headers:{
                "Content-Type" : "application/json"
            },
        })
        
        let reimbursements = await res.json()
        console.log(reimbursements);
        

        document.getElementById("reimbursementList").innerHTML = ""
        reimbursements.forEach(element => {
            createReimbursementRow(element, document.getElementById("reimbursementList"))
        });
    } catch (e) {
        console.log(e);
    }
}

function createReimbursementRow(reimbursement, parentElement){
    let card = document.createElement('div')
    card.className = `card card-${reimbursement.status.toLowerCase()}`
    card.dataset.id = reimbursement.id;

    //header
    let header = document.createElement('div')
    header.innerText = `Submitted on ${new Date(reimbursement.submitted).toLocaleDateString("en-US")}
    $${reimbursement.amount} for ${reimbursement.type.toLowerCase()}  by ${reimbursement.firstName.charAt(0).toUpperCase()}${reimbursement.firstName.slice(1).toLowerCase()} ${reimbursement.lastName.charAt(0).toUpperCase()}${reimbursement.lastName.slice(1).toLowerCase()}`
    header.className = "card-header"
    card.append(header)

//body
    let body = document.createElement('div')
    
    
    
    body.innerText =  `Description: ${reimbursement.description}`
    
    body.className = `card-body`
    card.append(body)
    
    
    let statusString = (reimbursement.status == 'PENDING')?'':` on ${new Date(reimbursement.resolved).toLocaleDateString("en-US")}`
//footer
    if(reimbursement.status == 'PENDING'){
        addButtons(card)
    }
    else{
        let footer  = document.createElement('div')
        footer.className = "card-footer"
        footer.innerText = `${reimbursement.status.charAt(0).toUpperCase() + reimbursement.status.slice(1).toLowerCase()}${statusString}`
        card.append(footer)
    }

    parentElement.appendChild(card)
}


function addButtons(card){
    // card.appendChild(document.createElement('br'))
    let footer  = document.createElement('div')
    footer.className = "card-footer"

    let approveBut = document.createElement('button')
    approveBut.className = "btn approve-button btn-success"
    approveBut.innerText = "Approve"

    approveBut.addEventListener("click", async (event) =>{
        event.preventDefault()
        console.log(event.target.parentElement)

        // console.log(reimbursement)
        let res = await fetch("http://localhost:8080/p1/manager/approvereq", {
            method:"POST",
            // body: `reimbursementid=1`,
            body: `reimbursementid=${event.target.parentElement.parentElement.dataset.id}`,
            headers:{
                "Content-Type" : "application/json"
            },
        })

        let newReimbursements = await res.json()
        event.target.parentElement.parentElement.className = "card card-approved"
        event.target.parentElement.innerText = "Approved"
       
        console.log(newReimbursements)
        }
    )

    footer.appendChild(approveBut)

    let declineBut = document.createElement('button')
    declineBut.className = "btn decline-button btn-danger"
    declineBut.innerText = "Decline"

    declineBut.addEventListener("click", async (event) =>{
        event.preventDefault()
        console.log(event.target.parentElement)

        
        let res = await fetch("http://localhost:8080/p1/manager/denyreq", {
            method:"POST",
            body: `reimbursementid=${event.target.parentElement.parentElement.dataset.id}`,
            headers:{
                "Content-Type" : "application/json"
            },
        })

        let newReimbursements = await res.json()
        event.target.parentElement.parentElement.className = "card card-denied"
        event.target.parentElement.innerText = "Denied"
        console.log(newReimbursements)
        }
    )
    
    footer.appendChild(declineBut)
    card.appendChild(footer)
    
}

async function authenticateUser() {
    try {
      let res = await fetch("http://localhost:8080/p1/login/financemanager", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
  
      let authorizationCheck = await res.text();
      console.log(authorizationCheck);
  
      if ("athenticated" !== authorizationCheck) {
        document.location.href = "login.html";
      }
    } catch (e) {
      console.log(e);
      document.location.href = "login.html";
    }
  }
  
  authenticateUser()
getAllTicketsByStatus()
document.getElementById("filterForm").addEventListener("submit", getAllTicketsByStatus)