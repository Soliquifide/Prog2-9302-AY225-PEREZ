
        const validCredentials = {
            'admin': 'password123',
            'helloworld': 'helloworld123',
            'beepboop': 'beep123',
            'javascript': 'javascript123'
        };

        let attendanceRecords = [];

        function playBeep() {
            const audioContext = new (window.AudioContext || window.webkitAudioContext)();
            const oscillator = audioContext.createOscillator();
            const gainNode = audioContext.createGain();

            oscillator.connect(gainNode);
            gainNode.connect(audioContext.destination);

            oscillator.frequency.value = 800;
            oscillator.type = 'sine';

            gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
            gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);

            oscillator.start(audioContext.currentTime);
            oscillator.stop(audioContext.currentTime + 0.5);
        }

        function getFormattedDateTime() {
            const now = new Date();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const year = now.getFullYear();
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const seconds = String(now.getSeconds()).padStart(2, '0');

            return `${month}/${day}/${year} ${hours}:${minutes}:${seconds}`;
        }

        function showMessage(text, type) {
            const messageDiv = document.getElementById('message');
            messageDiv.textContent = text;
            messageDiv.className = `message ${type}`;
            messageDiv.style.display = 'block';
        }

        document.getElementById('loginForm').addEventListener('submit', function(e) {
            e.preventDefault();

            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            if (validCredentials[username] && validCredentials[username] === password) {

                const timestamp = getFormattedDateTime();
                
                showMessage('Login successful! Welcome, ' + username + '!', 'success');

                const timestampDiv = document.getElementById('timestamp');
                timestampDiv.textContent = 'Login Time: ' + timestamp;
                timestampDiv.style.display = 'block';

                attendanceRecords.push({
                    username: username,
                    timestamp: timestamp
                });

                document.getElementById('attendanceSection').style.display = 'block';

                document.getElementById('loginForm').reset();
            } else {

                showMessage('Invalid username or password!', 'error');
                playBeep();

                document.getElementById('timestamp').style.display = 'none';
                document.getElementById('attendanceSection').style.display = 'none';
            }
        });

        document.getElementById('downloadBtn').addEventListener('click', function() {
            let attendanceData = '=== ATTENDANCE SUMMARY ===\n\n';
            
            attendanceRecords.forEach((record, index) => {
                attendanceData += `Record #${index + 1}\n`;
                attendanceData += `Username: ${record.username}\n`;
                attendanceData += `Timestamp: ${record.timestamp}\n`;
                attendanceData += `${'='.repeat(40)}\n\n`;
            });

            attendanceData += `Total Logins: ${attendanceRecords.length}\n`;
            attendanceData += `Generated: ${getFormattedDateTime()}\n`;

            const blob = new Blob([attendanceData], { type: 'text/plain' });
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = 'attendance_summary.txt';
            link.click();

            showMessage('Attendance summary downloaded successfully!', 'success');
        });