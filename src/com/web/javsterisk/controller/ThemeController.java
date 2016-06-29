package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.entity.Theme;

@ManagedBean
@SessionScoped
public class ThemeController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(ThemeController.class);
		            
    private String theme;
	
	private String defaultTheme;
	
	private List<Theme> themes;
	
	private SelectItem[] themesFilter;

	@PostConstruct
	public void initTheme() {
//		if(securityController.isAuthenticated()){
		
		themes = new ArrayList<Theme>(0);
    	themes.add(new Theme("afterdark", "afterdark.png"));
    	themes.add(new Theme("afternoon", "afternoon.png"));
    	themes.add(new Theme("afterwork", "afterwork.png"));
    	themes.add(new Theme("aristo", "aristo.png"));
    	themes.add(new Theme("black-tie", "black-tie.png"));
    	themes.add(new Theme("blitzer", "blitzer.png"));
        themes.add(new Theme("bluesky", "bluesky.png"));
        themes.add(new Theme("bootstrap", "bootstrap.png"));
        themes.add(new Theme("casablanca", "casablanca.png"));
        themes.add(new Theme("cruze", "cruze.png"));
        themes.add(new Theme("cupertino", "cupertino.png"));
        themes.add(new Theme("dark-hive", "dark-hive.png"));
        themes.add(new Theme("delta", "delta.png"));
        themes.add(new Theme("dot-luv", "dot-luv.png"));
        themes.add(new Theme("eggplant", "eggplant.png"));
        themes.add(new Theme("excite-bike", "excite-bike.png"));
        themes.add(new Theme("flick", "flick.png"));
        themes.add(new Theme("glass-x", "glass-x.png"));
        themes.add(new Theme("home", "home.png"));
        themes.add(new Theme("hot-sneaks", "hot-sneaks.png"));
        themes.add(new Theme("humanity", "humanity.png"));
        themes.add(new Theme("le-frog", "le-frog.png"));
        themes.add(new Theme("midnight", "midnight.png"));
        themes.add(new Theme("mint-choc", "mint-choc.png"));
        themes.add(new Theme("overcast", "overcast.png"));
        themes.add(new Theme("pepper-grinder", "pepper-grinder.png"));
        themes.add(new Theme("redmond", "redmond.png"));
        themes.add(new Theme("rocket", "rocket.png"));
        themes.add(new Theme("sam", "sam.png"));
        themes.add(new Theme("smoothness", "smoothness.png"));
        themes.add(new Theme("south-street", "south-street.png"));
        themes.add(new Theme("start", "start.png"));
        themes.add(new Theme("sunny", "sunny.png"));
        themes.add(new Theme("swanky-purse", "swanky-purse.png"));
        themes.add(new Theme("trontastic", "trontastic.png"));
        themes.add(new Theme("ui-darkness", "ui-darkness.png"));
        themes.add(new Theme("ui-lightness", "ui-lightness.png"));
        themes.add(new Theme("vader", "vader.png"));
				   
		   if(themes != null && themes.size() > 0){
			   themesFilter = new SelectItem[themes.size() + 1];
			   themesFilter[0] = new SelectItem("", "Select");
			   int i = 1;
			   for(Theme theme : themes){
				   themesFilter[i] = new SelectItem(theme.getName(), theme.getName());
				   i++;
			   }
		   }
		
		defaultTheme = "aristo";
		log.debug("Loading Default Theme : {}", defaultTheme);
		if(theme == null){						
			theme = defaultTheme;
			log.debug("Theme is null loading from current session : {}", theme);
		}
		log.debug("Loading Theme : {}", theme);	
//		}
	}
	
	public String getTheme() {		
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getDefaultTheme() {		
		return defaultTheme;
	}

	public void setDefaultTheme(String defaultTheme) {
		this.defaultTheme = defaultTheme;
	}

	public List<Theme> getThemes() {
		return themes;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public SelectItem[] getThemesFilter() {
		return themesFilter;
	}

	public void setThemesFilter(SelectItem[] themesFilter) {
		this.themesFilter = themesFilter;
	}
	
	
}
