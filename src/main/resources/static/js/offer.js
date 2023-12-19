let brandElement = document.getElementsByName("brandId")[0];
let modelElement = document.getElementsByName("modelId")[0];
let enginesElement = document.getElementsByName("engineId")[0];
let bodiesElement = document.getElementsByName("bodyType")[0];
let drivesElement = document.getElementsByName("driveType")[0];
let transmissionsElement = document.getElementsByName("transmissionType")[0];

document.addEventListener("DOMContentLoaded", function(event) {
    enableDisable(brandElement, modelElement)
    enableDisable(modelElement, enginesElement)
    enableDisable(modelElement, bodiesElement)
    enableDisable(modelElement, drivesElement)
    enableDisable(modelElement, transmissionsElement)
})


function fetchModels() {
    // Empty innerHTML for all fields except brand
    modelElement.innerHTML = '<option value="" disabled="" selected="">Select car model</option>'
    enginesElement.innerHTML = '<option value="" disabled="" selected="">Select car engine</option>'
    bodiesElement.innerHTML = '<option value="" disabled="" selected="">Select body type</option>'
    drivesElement.innerHTML = '<option value="" disabled="" selected="">Select drive type</option>'
    transmissionsElement.innerHTML = '<option value="" disabled="" selected="">Select transmission type</option>'

    // Unlock model field, other fields should remain locked
    enableDisable(brandElement, modelElement)
    enableDisable(modelElement, enginesElement)
    enableDisable(modelElement, bodiesElement)
    enableDisable(modelElement, drivesElement)
    enableDisable(modelElement, transmissionsElement)

    fetch('/api/v1/models/filter?brandId=' + brandElement.value)
        .then(response => response.json())
        .then(models => {
            for (const model of models) {
                let selectOption = document.createElement("option")
                selectOption.value = model.id
                // 3 Series (2004 - 2010)
                let text = document.createTextNode(`${model.name} (${model.startYear} - ${model.endYear})`)
                selectOption.appendChild(text)
                modelElement.append(selectOption)
            }
        })
}

function fetchModel() {
    // Empty innerHTML for all fields except brand and model
    enginesElement.innerHTML = '<option value="" disabled="" selected="">Select car engine</option>'
    bodiesElement.innerHTML = '<option value="" disabled="" selected="">Select body type</option>'
    drivesElement.innerHTML = '<option value="" disabled="" selected="">Select drive type</option>'
    transmissionsElement.innerHTML = '<option value="" disabled="" selected="">Select transmission type</option>'

    // Unlock all fields
    enableDisable(modelElement, enginesElement)
    enableDisable(modelElement, bodiesElement)
    enableDisable(modelElement, drivesElement)
    enableDisable(modelElement, transmissionsElement)

    fetch('/api/v1/models/' + modelElement.value)
        .then(response => response.json())
        .then(model => {
            for (const engine of model.engines) {
                let selectOption = document.createElement("option")
                selectOption.value = engine.id
                // M47TUD20 (DIESEL, 150HP, 1995cc)
                let text = document.createTextNode(`${engine.name} (${engine.fuelType}, ${engine.horsepower}HP, ${engine.displacement}cc)`)
                selectOption.appendChild(text)
                enginesElement.append(selectOption)
            }

            for (const bodyType of model.bodyTypes) {
                let selectOption = document.createElement("option")
                selectOption.value = bodyType
                // CONVERTIBLE
                let text = document.createTextNode(`${bodyType}`)
                selectOption.appendChild(text)
                bodiesElement.append(selectOption)
            }

            for (const driveType of model.driveTypes) {
                let selectOption = document.createElement("option")
                selectOption.value = driveType
                // AWD
                let text = document.createTextNode(`${driveType}`)
                selectOption.appendChild(text)
                drivesElement.append(selectOption)
            }

            for (const transmissionType of model.transmissionTypes) {
                let selectOption = document.createElement("option")
                selectOption.value = transmissionType
                // MANUAL
                let text = document.createTextNode(`${transmissionType}`)
                selectOption.appendChild(text)
                transmissionsElement.append(selectOption)
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