package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    String currentInput = "";
    String currentOperator = "";
    double result = 0;
    double previousResult = 0; // Almacenar el resultado anterior
    CountDownTimer divisionTimer; // Temporizador para la división
    CountDownTimer revertTimer; // Temporizador para revertir el resultado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        // Asigna los botones y configura el listener
        MaterialButton button0 = assignButton(R.id.button_0);
        MaterialButton button1 = assignButton(R.id.button_1);
        MaterialButton button2 = assignButton(R.id.button_2);
        MaterialButton button3 = assignButton(R.id.button_3);
        MaterialButton button4 = assignButton(R.id.button_4);
        MaterialButton button5 = assignButton(R.id.button_5);
        MaterialButton button6 = assignButton(R.id.button_6);
        MaterialButton button7 = assignButton(R.id.button_7);
        MaterialButton button8 = assignButton(R.id.button_8);
        MaterialButton button9 = assignButton(R.id.button_9);
        MaterialButton buttonClear = assignButton(R.id.button_c);
        MaterialButton buttonEquals = assignButton(R.id.button_resultSymbol);
        MaterialButton buttonPlusMinus = assignButton(R.id.button_positiveNegative);
        MaterialButton buttonPercent = assignButton(R.id.button_percent);
        MaterialButton buttonAdd = assignButton(R.id.button_sumSymbol);
        MaterialButton buttonSubtract = assignButton(R.id.button_minusSymbol);
        MaterialButton buttonMultiply = assignButton(R.id.button_multiSymbol);
        MaterialButton buttonDivide = assignButton(R.id.button_divideSymbol);

        // Implementa la lógica del botón "="
        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performCalculation();
                startResultDivisionTimer(); // Inicia el temporizador de división
            }
        });

        // Implementa la lógica del botón C (borrar)
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCalculator();
                cancelTimers(); // Cancela los temporizadores si se presiona "C"
            }
        });

        // Implementa la lógica del botón "+/-" (cambio de signo)
        buttonPlusMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSign();
            }
        });

        // Implementa la lógica del botón "%" (porcentaje)
        buttonPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatePercentage();
            }
        });
    }

    MaterialButton assignButton(int id) {
        MaterialButton btn = findViewById(id);
        btn.setOnClickListener(this);
        return btn;
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();

        // Implementa el manejo de clics de botones numéricos, operadores y botones especiales aquí
        if (isNumeric(buttonText)) {
            currentInput += buttonText;
            updateResultView(currentInput);
        } else if (isOperator(buttonText)) {
            handleOperator(buttonText);
        }
    }

    private void performCalculation() {
        if (!currentInput.isEmpty() && !currentOperator.isEmpty()) {
            double secondOperand = Double.parseDouble(currentInput);
            switch (currentOperator) {
                case "+":
                    result += secondOperand;
                    break;
                case "-":
                    result -= secondOperand;
                    break;
                case "x":
                    result *= secondOperand;
                    break;
                case "÷":
                    if (secondOperand != 0) {
                        result /= secondOperand;
                    } else {
                        // Manejar división por cero aquí
                    }
                    break;
            }
            updateResultView(String.valueOf(result));
            currentInput = "";
            currentOperator = "";
        }
    }

    private void clearCalculator() {
        currentInput = "";
        currentOperator = "";
        result = 0;
        updateResultView("0");
    }

    private void toggleSign() {
        if (!currentInput.isEmpty()) {
            double currentValue = Double.parseDouble(currentInput);
            currentValue = -currentValue;
            currentInput = String.valueOf(currentValue);
            updateResultView(currentInput);
        }
    }

    private void calculatePercentage() {
        if (!currentInput.isEmpty()) {
            double percentage = Double.parseDouble(currentInput) / 100.0;
            currentInput = String.valueOf(percentage);
            updateResultView(currentInput);
        }
    }

    private void handleOperator(String operator) {
        if (!currentInput.isEmpty()) {
            currentOperator = operator;
            result = Double.parseDouble(currentInput);
            currentInput = "";
        }
    }

    private void updateResultView(String text) {
        resultTv.setText(text);
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("x") || str.equals("÷");
    }

    // Función para iniciar el temporizador de división
    private void startResultDivisionTimer() {
        // Configurar el temporizador para dividir el resultado después de 5 segundos
        divisionTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Cuenta regresiva para la división
            }

            public void onFinish() {
                // Almacena el resultado actual como resultado anterior
                previousResult = result;
                // Divide el resultado por 9
                if (result != 0) {
                    result /= 9;
                    updateResultView(String.valueOf(result));
                    // Inicia el temporizador para revertir el resultado después de 5 segundos
                    startRevertTimer();
                }
            }
        }.start();
    }

    // Función para iniciar el temporizador de reversión del resultado
    private void startRevertTimer() {
        // Configura el temporizador para revertir el resultado después de 5 segundos
        revertTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Cuenta regresiva para la reversión
            }

            public void onFinish() {
                // Restablece el resultado al valor anterior
                result = previousResult;
                updateResultView(String.valueOf(result));
            }
        }.start();
    }

    // Función para cancelar los temporizadores
    private void cancelTimers() {
        if (divisionTimer != null) {
            divisionTimer.cancel();
        }
        if (revertTimer != null) {
            revertTimer.cancel();
        }
    }
}
