package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.app.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    StringBuilder inputHistory = new StringBuilder(); // Historial de entrada
    String currentInput = "";
    String currentOperator = "";
    double result = 0;
    private boolean actionButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

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
        MaterialButton buttonAction = assignButton(R.id.button_Action);
        MaterialButton buttonPlusMinus = assignButton(R.id.button_positiveNegative);
        MaterialButton buttonPercent = assignButton(R.id.button_percent);
        MaterialButton buttonAdd = assignButton(R.id.button_sumSymbol);
        MaterialButton buttonSubtract = assignButton(R.id.button_minusSymbol);
        MaterialButton buttonMultiply = assignButton(R.id.button_multiSymbol);
        MaterialButton buttonDivide = assignButton(R.id.button_divideSymbol);

        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performStandardCalculation();
            }
        });

        buttonAction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Cuando se presiona el botón "Action"
                        actionButtonPressed = true;
                        performNewCalculation();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Cuando se suelta el botón "Action"
                        actionButtonPressed = false;
                        updateResultView(currentInput);
                        break;
                }
                return true;
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCalculator();
            }
        });

        buttonPlusMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSign();
            }
        });

        buttonPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatePercentage();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOperator("+");
            }
        });

        buttonSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOperator("-");
            }
        });

        buttonMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOperator("x");
            }
        });

        buttonDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOperator("÷");
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
        String buttonText= button.getText().toString();


        // Agregar el contenido del botón al historial
        inputHistory.append(buttonText);

        // Si es un número, un operador o cualquier otro carácter, actualizar la entrada actual
        currentInput += buttonText;
        updateResultView(currentInput);
        // Actualizar la vista del historial
        updateSolutionView(inputHistory.toString());
    }


    private void performStandardCalculation() {
        if (!currentInput.isEmpty()) {
            if (!currentOperator.isEmpty()) {
                performCalculation();
                // Agrega el resultado actual al input actual
                currentInput = String.valueOf(result);
                updateResultView(currentInput);
                currentOperator = "";
            }
        }
    }

    private void performNewCalculation() {
        if (!currentInput.isEmpty()) {
            double originalValue = Double.parseDouble(currentInput);
            double result = originalValue / 7;
            String resultString = String.valueOf(result);

            // Agregar "9" al final del resultado
            resultString = resultString + "7";

            updateResultView(resultString);
        }
    }

    private void handleOperator(String operator) {
        if (!currentInput.isEmpty()) {
            if (!currentOperator.isEmpty()) {
                performCalculation();
                updateResultView(String.valueOf(result));
            } else {
                result = Double.parseDouble(currentInput);
            }
            currentOperator = operator;
            currentInput = "";
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
                        // Handle division by zero here
                    }
                    break;
            }
            updateResultView(String.valueOf(result));
            currentInput = "";
        }
    }

    private void clearCalculator() {
        currentInput = "";
        currentOperator = "";
        result = 0;
        updateResultView("0");

        // Limpiar el historial
        inputHistory.setLength(0);
        updateSolutionView(""); // Actualizar el historial a un valor vacío
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

    private void updateResultView(String text) {
        // Verifica si el resultado tiene un decimal sin valores significativos
        if (text.matches("\\d+\\.0")) {
            text = text.replace(".0", "");
        }
        resultTv.setText(text);
    }

    private void updateSolutionView(String text) {
        solutionTv.setText(text);
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
        return "+".equals(str) || "-".equals(str) || "x".equals(str) || "÷".equals(str);
    }
}
