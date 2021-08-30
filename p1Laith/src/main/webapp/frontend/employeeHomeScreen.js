async function submitReimbursement(event) {
  event.preventDefault();
  let amount = document.getElementById("amountInput").value;
  let type = document.getElementById("expenseTypeSelect").value;
  let description = document.getElementById("descriptionInput").value;

  let newReimbursement = {
    amount,
    type,
    description,
  };
  console.log(newReimbursement);

  try {
    let res = await fetch("http://localhost:8080/p1/employee/submit", {
      method: "POST",
      body: JSON.stringify(newReimbursement),
      headers: {
        "Content-Type": "application/json",
      },
    });

    let reimbursement = await res.json();
    console.log(reimbursement);
    alert("Reimbursement Submitted")
    document.getElementById("reimbursementForm").reset();
  } catch (e) {
    console.log(e);
  }
}

async function getAllTickets(event) {
  event.preventDefault();

  try {
    let res = await fetch(
      "http://localhost:8080/p1/employee/reimbursements",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    let reimbursements = await res.json();
    console.log(reimbursements);

    document.getElementById("ticketList").innerHTML = "";

    reimbursements.forEach((element) => {
      createReimbursementRow(element, document.getElementById("ticketList"));
    });
  } catch (e) {
    console.log(e);
  }
}

function createReimbursementRow(reimbursement, parentElement) {
  let card = document.createElement("div");
  card.className = `card card-${reimbursement.status.toLowerCase()}`;
  card.dataset.id = reimbursement.id;

  //header
  let header = document.createElement("div");
  header.innerText = `Submitted on ${new Date(
    reimbursement.submitted
  ).toLocaleDateString("en-US")}
  $${reimbursement.amount} for ${reimbursement.type.toLowerCase()}`

  header.className = "card-header";
  card.append(header);

  //body
  let body = document.createElement("div");

  body.innerText = `Description: ${reimbursement.description}`;

  body.className = `card-body`;
  card.append(body);

  let statusString =
    reimbursement.status == "PENDING"
      ? ""
      : ` on ${new Date(reimbursement.resolved).toLocaleDateString("en-US")}`;
  //footer
  let footer = document.createElement("div");
  footer.className = "card-footer";
  footer.innerText = `${
    reimbursement.status.charAt(0).toUpperCase() +
    reimbursement.status.slice(1).toLowerCase()
  }${statusString}`;
  card.append(footer);

  parentElement.appendChild(card);
}

async function authenticateUser() {
  try {
    let res = await fetch("http://localhost:8080/p1/login/employee", {
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

document
  .getElementById("reimbursementForm")
  .addEventListener("submit", submitReimbursement);
document.getElementById("getTickets").addEventListener("click", getAllTickets);
