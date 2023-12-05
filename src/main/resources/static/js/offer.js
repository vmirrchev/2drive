function fetchModels() {
    let selectedElement = document.getElementsByName("brandId")[0];
    let targetElement = document.getElementsByName("modelId")[0];

    fetch('/api/v1/models/filter?brandId=' + selectedElement.value)
        .then(response => response.json())
        .then(models => {
            targetElement.innerHTML = '<option value="" disabled="" selected="">Select car model</option>'
            for (const model of models) {
                let selectOption = document.createElement("option")
                selectOption.value = model.id
                // 3 Series (2004 - 2010)
                let text = document.createTextNode(`${model.name} (${model.startYear} - ${model.endYear})`)
                selectOption.appendChild(text)
                targetElement.append(selectOption)
            }
        })
}

function fetchModel() {
    let selectedElement = document.getElementsByName("modelId")[0];
    let enginesElement = document.getElementsByName("engineId")[0];
    let bodiesElement = document.getElementsByName("bodyType")[0];
    let drivesElement = document.getElementsByName("driveType")[0];
    let transmissionsElement = document.getElementsByName("transmissionType")[0];

    fetch('/api/v1/models/' + selectedElement.value)
        .then(response => response.json())
        .then(model => {
            enginesElement.innerHTML = '<option value="" disabled="" selected="">Select car engine</option>'
            for (const engine of model.engines) {
                let selectOption = document.createElement("option")
                selectOption.value = engine.id
                // M47TUD20 (DIESEL, 150HP, 1995cc)
                let text = document.createTextNode(`${engine.name} (${engine.fuelType}, ${engine.horsepower}HP, ${engine.displacement}cc)`)
                selectOption.appendChild(text)
                enginesElement.append(selectOption)
            }

            bodiesElement.innerHTML = '<option value="" disabled="" selected="">Select body type</option>'
            for (const bodyType of model.bodyTypes) {
                let selectOption = document.createElement("option")
                selectOption.value = bodyType
                // CONVERTIBLE
                let text = document.createTextNode(`${bodyType}`)
                selectOption.appendChild(text)
                bodiesElement.append(selectOption)
            }

            drivesElement.innerHTML = '<option value="" disabled="" selected="">Select drive type</option>'
            for (const driveType of model.driveTypes) {
                let selectOption = document.createElement("option")
                selectOption.value = driveType
                // AWD
                let text = document.createTextNode(`${driveType}`)
                selectOption.appendChild(text)
                drivesElement.append(selectOption)
            }

            transmissionsElement.innerHTML = '<option value="" disabled="" selected="">Select transmission type</option>'
            for (const transmissionType of model.transmissionTypes) {
                let selectOption = document.createElement("option")
                selectOption.value = transmissionType
                // AWD
                let text = document.createTextNode(`${transmissionType}`)
                selectOption.appendChild(text)
                transmissionsElement.append(selectOption)
            }
        })
}