function fetchEngines() {
    let selectedElement = document.getElementsByName("brandId")[0];
    let targetElements = document.getElementsByName("engineIds");

    fetch('/v1/api/engines/filter?brandId=' + selectedElement.value)
        .then(response => response.json())
        .then(engines => {
            for (const targetElement of targetElements) {
                targetElement.innerHTML = '<option value="" disabled="" selected="">Select engine</option>'
                for (const engine of engines) {
                    let selectOption = document.createElement("option")
                    selectOption.value = engine.id
                    // M47TUD20 (DIESEL, 150HP, 1995cc)
                    let text = document.createTextNode(`${engine.name} (${engine.fuelType}, ${engine.horsepower}HP, ${engine.displacement}cc)`)
                    selectOption.appendChild(text)
                    targetElement.append(selectOption)
                }
            }
        })
}

