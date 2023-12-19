let brandElement = document.getElementsByName("brandId")[0];
let enginesElement = document.getElementsByName("engineIds")[0];

document.addEventListener("DOMContentLoaded", function(event) {
    enableDisable(brandElement, enginesElement)
})

function fetchEngines() {
    // Empty innerHTML for all fields except brand
    enginesElement.innerHTML = '<option value="" disabled="" selected="">Select engine</option>'

    // Unlock engines field
    enableDisable(brandElement, enginesElement)

    fetch('/api/v1/engines/filter?brandId=' + brandElement.value)
        .then(response => response.json())
        .then(engines => {
            for (const engine of engines) {
                let selectOption = document.createElement("option")
                selectOption.value = engine.id
                // M47TUD20 (DIESEL, 150HP, 1995cc)
                let text = document.createTextNode(`${engine.name} (${engine.fuelType}, ${engine.horsepower}HP, ${engine.displacement}cc)`)
                selectOption.appendChild(text)
                enginesElement.append(selectOption)
            }
        })
}

function enableDisable(firstElement, secondElement) {
    if (firstElement.value == "") {
        secondElement.setAttribute('disabled','disabled');
    } else {
        secondElement.removeAttribute('disabled');
    }
}

