let inputQuantity = document.querySelector("#quantity");
let inputInstitution = document.querySelector("input[type=radio]:checked");
let inputStreet = document.querySelector("#street");
let inputCity = document.querySelector("#city");
let inputZipCode = document.querySelector("#zipCode");
let inputPhone = document.querySelector("#phone");
let inputPickUpDate = document.querySelector("#pickUpDate");
let inputPickUpTime = document.querySelector("#pickUpTime");
let inputPickUpComment = document.querySelector("#pickUpComment");

let summaryQuantity = document.querySelector("#summary-quantity");
let summaryInstitution = document.querySelector("#summary-institution");
let summaryStreet = document.querySelector("#summary-street");
let summaryCity = document.querySelector("#summary-city");
let summaryZipCode = document.querySelector("#summary-zipCode");
let summaryPhone = document.querySelector("#summary-phone");
let summaryPickUpDate = document.querySelector("#summary-pickUpDate");
let summaryPickUpTime = document.querySelector("#summary-pickUpTime");
let summaryPickUpComment = document.querySelector("#summary-pickUpComment");

let btnNextStep = document.querySelectorAll(".btn.next-step");

btnNextStep.forEach(value => {
    value.addEventListener("click", function () {
        inputInstitution = document.querySelector("input[type=radio]:checked");

        if (inputInstitution != null) {
            summaryInstitution.innerText = inputInstitution.parentElement.children[2].children[0].innerText;
        }

        summaryQuantity.innerText = inputQuantity.value;
        summaryStreet.innerText = inputStreet.value;
        summaryCity.innerText = inputCity.value;
        summaryZipCode.innerText = inputZipCode.value;
        summaryPhone.innerText = inputPhone.value;
        summaryPickUpDate.innerText = inputPickUpDate.value;
        summaryPickUpTime.innerText = inputPickUpTime.value;
        summaryPickUpComment.innerText = inputPickUpComment.value;
    })
});
