function calculatePrelimGrade() {
    const attendance = parseFloat(document.getElementById('attendance').value);
    const lab1 = parseFloat(document.getElementById('lab1').value);
    const lab2 = parseFloat(document.getElementById('lab2').value);
    const lab3 = parseFloat(document.getElementById('lab3').value);
    
    const outputDiv = document.getElementById('output');
    
    if (isNaN(attendance) || isNaN(lab1) || isNaN(lab2) || isNaN(lab3)) {
        outputDiv.innerHTML = '<div class="error">Error: Please enter valid numeric values for all fields.</div>';
        return;
    }
    
    if (attendance < 0 || lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
        outputDiv.innerHTML = '<div class="error">Error: Invalid input values.<br>Attendance must be >= 0<br>Lab grades must be between 0 and 100</div>';
        return;
    }
    
    const labWorkAverage = (lab1 + lab2 + lab3) / 3;
    
    const classStanding = (attendance * 0.40) + (labWorkAverage * 0.60);
    
    const requiredExamToPass = (75 - (classStanding * 0.70)) / 0.30;
    
    const requiredExamForExcellent = (100 - (classStanding * 0.70)) / 0.30;
    
    let output = `
        <div class="result-container">
            <h2>PRELIM GRADE CALCULATION</h2>
            
            <div class="section">
                <h3>INPUT VALUES:</h3>
                <p><strong>Attendance:</strong> ${attendance.toFixed(2)}</p>
                <p><strong>Lab Work 1:</strong> ${lab1.toFixed(2)}</p>
                <p><strong>Lab Work 2:</strong> ${lab2.toFixed(2)}</p>
                <p><strong>Lab Work 3:</strong> ${lab3.toFixed(2)}</p>
            </div>
            
            <div class="section">
                <h3>COMPUTED RESULTS:</h3>
                <p><strong>Lab Work Average:</strong> ${labWorkAverage.toFixed(2)}</p>
                <p><strong>Class Standing:</strong> ${classStanding.toFixed(2)}</p>
            </div>
            
            <div class="section">
                <h3>REQUIRED PRELIM EXAM SCORES:</h3>
                <p><strong>To Pass (75):</strong> ${requiredExamToPass.toFixed(2)}</p>
                <p><strong>For Excellent (100):</strong> ${requiredExamForExcellent.toFixed(2)}</p>
            </div>
            
            <div class="section remarks">
                <h3>REMARKS:</h3>
    `;
    
    if (requiredExamToPass <= 100 && requiredExamToPass >= 0) {
        output += `<p class="success">✓ You need ${requiredExamToPass.toFixed(2)}% on the Prelim Exam to pass.</p>`;
    } else if (requiredExamToPass < 0) {
        output += `<p class="success">✓ You have already secured a passing grade!</p>`;
    } else {
        output += `<p class="error">✗ It is not possible to pass the Prelim period.<br>(Required score exceeds 100%)</p>`;
    }
    
    if (requiredExamForExcellent <= 100 && requiredExamForExcellent >= 0) {
        output += `<p class="success">✓ You need ${requiredExamForExcellent.toFixed(2)}% on the Prelim Exam for excellent.</p>`;
    } else if (requiredExamForExcellent < 0) {
        output += `<p class="success">✓ You have already achieved excellent standing!</p>`;
    } else {
        output += `<p class="error">✗ It is not possible to achieve excellent standing.<br>(Required score exceeds 100%)</p>`;
    }
    
    output += `
            </div>
        </div>
    `;
    
    outputDiv.innerHTML = output;
}

function clearForm() {
    document.getElementById('attendance').value = '';
    document.getElementById('lab1').value = '';
    document.getElementById('lab2').value = '';
    document.getElementById('lab3').value = '';
    document.getElementById('output').innerHTML = '';
}

function limitInput(input) {
    const max = parseFloat(input.getAttribute('max'));
    const min = parseFloat(input.getAttribute('min'));
    const value = parseFloat(input.value);
    
    if (max !== null && value > max) {
        input.value = max;
    }
    if (min !== null && value < min) {
        input.value = min;
    }
}

function clearForm() {
    document.getElementById('attendance').value = '';
    document.getElementById('lab1').value = '';
    document.getElementById('lab2').value = '';
    document.getElementById('lab3').value = '';
    document.getElementById('output').innerHTML = '';
}

