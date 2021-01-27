package ui;

import components.OnClickListener;
import components.Part;
import components.View;
import core.JUIXApplication;
import views.ButtonView;
import views.GraphView;
import views.MoleculesView;
import views.TextView;

import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

public class MainPart extends Part {
    private TextView pocetCelkem, pocetPremenenych, pocetPuvodnich, pocetOpakovani;
    private MoleculesView moleculesView;
    private GraphView graph;
    ButtonView button;
    private boolean running = false;
    int opakovani = 0;

    public MainPart(JUIXApplication application) {
        super(application);
    }

    @Override
    protected void onCreate() {
        File file = null;
        try {
            file = new File(getClass().getClassLoader().getResource("layouts/main_layout.xml").toURI());
        } catch (URISyntaxException | NullPointerException e) {
            e.printStackTrace();
        }
        setLayout(file);
        pocetCelkem = (TextView) layout.getView("pocetCelkem");
        pocetPremenenych = (TextView) layout.getView("pocetPremenenych");
        pocetPuvodnich = (TextView) layout.getView("pocetPuvodnich");
        pocetOpakovani = (TextView) layout.getView("pocetOpakovani");
        moleculesView = (MoleculesView) layout.getView("moleculesView");
        graph = (GraphView) layout.getView("graph");
        graph.addData(moleculesView.getTotalMolecules());

        pocetCelkem.setText("Počet atomu celkem: " + moleculesView.getTotalMolecules());
        pocetPremenenych.setText("Počet přeměněných atomu: " + moleculesView.getNumberOfConverted());
        pocetPuvodnich.setText("Počet nepřeměněných atomu: " + moleculesView.getNumberOfUnconverted());
        pocetOpakovani.setText("Počet opakování: "+opakovani);

        button = (ButtonView) layout.getView("button");
        button.addOnClickListener(view -> {
            if(!running) {
                running = true;
                moleculesView.run();
                button.setText("Stop");
                graph.clearData();
                graph.addData(moleculesView.getNumberOfUnconverted());
                opakovani = 0;
            }else{
                running = false;
                moleculesView.stopRunning();
                button.setText("Start");
            }
        });
    }

    @Override
    public void update(int tick) {
        super.update(tick);
        if(running) {
            opakovani++;
            pocetCelkem.setText("Počet atomu celkem: " + moleculesView.getTotalMolecules());
            pocetPremenenych.setText("Počet přeměněných atomu: " + moleculesView.getNumberOfConverted());
            pocetPuvodnich.setText("Počet nepřeměněných atomu: " + moleculesView.getNumberOfUnconverted());
            pocetOpakovani.setText("Počet opakování: "+opakovani);
            graph.addData(moleculesView.getNumberOfUnconverted());
            if(moleculesView.getNumberOfUnconverted()==0){
                running = false;
                button.setText("Start");
            }
        }

    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }
}
